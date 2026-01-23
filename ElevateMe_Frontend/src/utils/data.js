import TEMPLATE_ONE_IMG from '../assets/template-one.png'
import TEMPLATE_TWO_IMG from '../assets/template-two.png'
import TEMPLATE_THREE_IMG from '../assets/template-three.png'

export const resumeTemplates = [
  {
    id:'01',
    thumbnailImg: TEMPLATE_ONE_IMG,
    colorPaletteCode: 'themeOne',
    isPremium: false
  },
  {
    id:'02',
    thumbnailImg: TEMPLATE_TWO_IMG,
    colorPaletteCode: 'themeTwo',
    isPremium: true
  },
  {
    id:'03',
    thumbnailImg: TEMPLATE_THREE_IMG,
    colorPaletteCode: 'themeThree',
    isPremium: true
  }
]

export const themeColorPalette = {
  themeOne: [
    ["#EBFDFF", "#A1F4FD", "#CEFAFE", "#00B8DB", "#4A5565"],

    ["#E9FBF8", "#B4EFE7", "#93E2DA", "#2AC9A0", "#3D4C5A"],
    ["#F5F4FF", "#E0DBFF", "#C9C2F8", "#8579D1", "#4B4B5C"],
    ["#F0FAFF", "#D6F0FF", "#AFDEFF", "#3399FF", "#445361"],
    ["#FFF5F7", "#FFE0EC", "#FAC6D4", "#F6729C", "#5A5A5A"],
    ["#F9FAFB", "#E4E7EB", "#CBD5E0", "#7F9CF5", "#2D3748"],

    ["#F4FFFD", "#D3FDF2", "#B0E9D4", "#34C79D", "#384C48"],
    ["#FFF7F0", "#FFE6D9", "#FFD2BA", "#FF9561", "#4C4743"],
    ["#F9FCFF", "#E3F0F9", "#C0DDEE", "#6CA6CF", "#46545E"],
    ["#FFFDF6", "#FFF4D7", "#FFE7A0", "#FFD000", "#57534E"],
    ["#EFFCFF", "#C8F0FF", "#99E0FF", "#007BA7", "#2B3A42"],

    ["#F7F7F7", "#E4E4E4", "#CFCFCF", "#4A4A4A", "#222222"],
    ["#E3F2FD", "#90CAF9", "#a8d2f4", "#1E88E5", "#0D47A1"],
  ],
  themeTwo: [
    ["#E9FBF8", "#B4EFE7", "#93E2DA", "#2AC9A0", "#3D4C5A"],
    ["#F5F4FF", "#E0DBFF", "#C9C2F8", "#8579D1", "#4B4B5C"],
    ["#F0FAFF", "#D6F0FF", "#AFDEFF", "#3399FF", "#445361"],
    ["#FFF5F7", "#FFE0EC", "#FAC6D4", "#F6729C", "#5A5A5A"],
    ["#F9FAFB", "#E4E7EB", "#CBD5E0", "#7F9CF5", "#2D3748"],
    ["#F4FFFD", "#D3FDF2", "#B0E9D4", "#34C79D", "#384C48"],
    ["#FFF7F0", "#FFE6D9", "#FFD2BA", "#FF9561", "#4C4743"],
    ["#F9FCFF", "#E3F0F9", "#C0DDEE", "#6CA6CF", "#46545E"],
    ["#FFFDF6", "#FFF4D7", "#FFE7A0", "#FFD000", "#57534E"],
    ["#EFFCFF", "#C8F0FF", "#99E0FF", "#007BA7", "#2B3A42"],
    ["#F7F7F7", "#E4E4E4", "#CFCFCF", "#4A4A4A", "#222222"],
    ["#E3F2FD", "#90CAF9", "#a8d2f4", "#1E88E5", "#0D47A1"],
  ],
  themeThree: [
    ["#F5F4FF", "#E0DBFF", "#C9C2F8", "#8579D1", "#4B4B5C"],
    ["#F0FAFF", "#D6F0FF", "#AFDEFF", "#3399FF", "#445361"],
    ["#FFF5F7", "#FFE0EC", "#FAC6D4", "#F6729C", "#5A5A5A"],
    ["#F9FAFB", "#E4E7EB", "#CBD5E0", "#7F9CF5", "#2D3748"],
    ["#F4FFFD", "#D3FDF2", "#B0E9D4", "#34C79D", "#384C48"],
    ["#FFF7F0", "#FFE6D9", "#FFD2BA", "#FF9561", "#4C4743"],
    ["#F9FCFF", "#E3F0F9", "#C0DDEE", "#6CA6CF", "#46545E"],
    ["#FFFDF6", "#FFF4D7", "#FFE7A0", "#FFD000", "#57534E"],
    ["#EFFCFF", "#C8F0FF", "#99E0FF", "#007BA7", "#2B3A42"],
    ["#F7F7F7", "#E4E4E4", "#CFCFCF", "#4A4A4A", "#222222"],
    ["#E3F2FD", "#90CAF9", "#a8d2f4", "#1E88E5", "#0D47A1"],
    ["#E9FBF8", "#B4EFE7", "#93E2DA", "#2AC9A0", "#3D4C5A"],
  ]
};

export const DUMMY_RESUME_DATA = {
  profileInfo: {
    profileImg: null,
    previewUrl: "",
    fullName: "Bushan SC",
    designation: "Senior Software Engineer",
    summary:
      "Passionate and results-driven developer with 8+ years of experience building scalable web applications using modern technologies like Java, Spring Boot, Microservices, AWS.",
  },
  contactInfo: {
    email: "bushansc@gmail.com",
    phone: "+9874561230",
    location:'Banglore, India',
    linkedin: "https://linkedin.com/in/bushan-sc",
    github: "https://github.com/scbushan05",
    website: "https://youtube.com/channel/UCff5tZ-js4bu68GQB_EhW9w",
  },
  workExperience: [
    {
      company: "IBM Global Business Services",
      role: "Cloud Full Stack Engineer",
      startDate: "2022-01",
      endDate: "2025-10",
      description:
        "ply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make.",
    },
    {
      company: "Cardinal Health India International",
      role: "Senior Developer",
      startDate: "2020-01",
      endDate: "2022-01",
      description:
        "ply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make.",
    },
    {
      company: "Cognizant Technology Solutions",
      role: "Associate Engineer",
      startDate: "2018-06",
      endDate: "2020-01",
      description:
        "ply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make.",
    },
  ],
  education: [
    {
      degree: "Masters in Computer Science",
      institution: "VTU University",
      startDate: "2013-07",
      endDate: "2016-06",
    },
    {
      degree: "Bachelors in Computer Science",
      institution: "Kuvempu University",
      startDate: "2010-08",
      endDate: "2013-06",
    },
    {
      degree: "Biology in Secondary High School",
      institution: "D R M Science College",
      startDate: "2008-06",
      endDate: "2010-05",
    },
  ],
  skills: [
    { name: "Java", progress: 85 },
    { name: "React", progress: 90 },
    { name: "SQL", progress: 75 },
    { name: "Problem Solving", progress: 70 },
    { name: "MongoDB", progress: 75 },
  ],
  projects: [
    {
      title: "Prescription Management",
      description:
        "ply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make.",
      github: "https://github.com/scbushan05/prescription-manager",
    },
    {
      title: "Campaign Management",
      description:
        "ply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make.",
      liveDemo: "https://github.com/scbushan05/campaign-manager",
    },
    {
      title: "Analytics Dashboard",
      description:
        "ply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make.",
      github: "https://github.com/scbushan05/analytics-dashboard",
      liveDemo: "https://analytics-dashboard.dev",
    },
  ],
  certifications: [
    {
      title: "Oracle Java Certification",
      issuer: "Oracle",
      year: "2022",
    },
    {
      title: "AWS Practitioner",
      issuer: "AWS",
      year: "2020",
    },
  ],
  languages: [
    { name: "English", progress: 100 },
    { name: "Hindi", progress: 70 },
    { name: "Kannada", progress: 40 },
  ],
  interests: ["Cycling", "Content Creation", "Teaching"],
};