import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { fixTailwindColors } from './helper';

// Size helpers
const MB = 1024 * 1024;
const SIZE_LIMIT_MB = 18; // keep some headroom below 20MB SMTP limit

// Sensible defaults
const DEFAULTS = {
  quality: 0.85, // JPEG quality (0..1)
  scale: 2,      // html2canvas scale
};

export const generatePDFFromElement = async (element, filename = 'resume.pdf', options = {}) => {
  const quality = typeof options.quality === 'number' ? options.quality : DEFAULTS.quality;
  const scale = typeof options.scale === 'number' ? options.scale : DEFAULTS.scale;

  let clone = null;
  try {
    // Clone the element so we don't mutate the live UI
    clone = element.cloneNode(true);
    clone.style.position = 'fixed';
    clone.style.left = '-10000px';
    clone.style.top = '0';
    clone.style.background = '#ffffff';
    clone.style.width = `${element.scrollWidth}px`;

    document.body.appendChild(clone);

    // Tailwind v4 uses oklch colors that html2canvas can't parse; normalize to rgb
    fixTailwindColors(clone);

    // Render to canvas
    const canvas = await html2canvas(clone, {
      scale,
      useCORS: true,
      allowTaint: true,
      backgroundColor: '#ffffff',
      width: clone.scrollWidth,
      height: clone.scrollHeight,
      scrollX: 0,
      scrollY: 0,
    });

    // Convert to JPEG (much smaller than PNG)
    const dataUrl = canvas.toDataURL('image/jpeg', quality);

    // Prepare PDF paging
    const imgWidth = 210;  // A4 width in mm
    const pageHeight = 295; // A4 height in mm
    const imgHeight = (canvas.height * imgWidth) / canvas.width;
    let heightLeft = imgHeight;

    const pdf = new jsPDF('p', 'mm', 'a4');
    let position = 0;

    // First page
    pdf.addImage(dataUrl, 'JPEG', 0, position, imgWidth, imgHeight);
    heightLeft -= pageHeight;

    // Additional pages (repeat image with offset)
    while (heightLeft > 0) {
      position = heightLeft - imgHeight;
      pdf.addPage();
      pdf.addImage(dataUrl, 'JPEG', 0, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;
    }

    return pdf;
  } catch (error) {
    console.error('Error generating PDF:', error);
    throw new Error('Failed to generate PDF');
  } finally {
    if (clone && clone.parentNode) {
      clone.parentNode.removeChild(clone);
    }
  }
};

export const generatePDFBlob = async (element, options = {}) => {
  // Try with provided or default settings
  let pdf = await generatePDFFromElement(element, 'resume.pdf', options);
  let blob = pdf.output('blob');

  // If still too large, try progressively lowering quality/scale
  if (blob.size > SIZE_LIMIT_MB * MB) {
    // Lower quality first
    pdf = await generatePDFFromElement(element, 'resume.pdf', { ...options, quality: 0.7 });
    blob = pdf.output('blob');
  }
  if (blob.size > SIZE_LIMIT_MB * MB) {
    // Lower quality and scale as last resort
    pdf = await generatePDFFromElement(element, 'resume.pdf', { ...options, quality: 0.6, scale: 1.4 });
    blob = pdf.output('blob');
  }

  return blob;
};

export const downloadPDF = async (element, filename = 'resume.pdf', options = {}) => {
  const pdf = await generatePDFFromElement(element, filename, options);
  pdf.save(filename.endsWith('.pdf') ? filename : `${filename}.pdf`);
};
