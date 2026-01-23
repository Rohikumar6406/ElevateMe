package in.ElevateMe.service;

import in.ElevateMe.Document.Resume;
import in.ElevateMe.dto.AuthResponse;
import in.ElevateMe.dto.CreateResumeRequest;
import in.ElevateMe.repository.ResumeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final AuthService authService;

    public Resume createResume(CreateResumeRequest request, Object principalObject) {
        //Step 1: Create resume Object
        Resume newResume = new Resume();

        //step2: Get current profile
        AuthResponse response = authService.getProfile(principalObject);

        //step3: update the resume object
        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());


        //step4: set default data for resume

        setDefaultResumeData(newResume);

        //step 5: save the resume data
        return resumeRepository.save(newResume);

    }
    private void setDefaultResumeData(Resume newResume){
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducations(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
    }
    public List<Resume> getUserResumes(Object principal){
        //Step 1: Get the current profile
        AuthResponse response = authService.getProfile(principal);

        //Step2: Call the repository finder method
        List<Resume> resumes=resumeRepository.findByUserIdOrderByUpdatedAtDesc(response.getId());

        //step3: return response
        return resumes;
    }

    public Resume getResumeById(String resumeId, Object principal){

        //Step1: Get the current profile id
        AuthResponse response = authService.getProfile(principal);

        //Step2:Call the repo finder method
        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(()->new RuntimeException("Resume not found"));

        //Step3: return the result
        return existingResume;

    }
    public Resume updateResume(String resumeId, Resume updatedData, Object principal){
        //Step 1: Get the current profile
        AuthResponse response=authService.getProfile(principal);


        //Step2: Call the repository finder method
       Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
               .orElseThrow(()->new RuntimeException("Resume not found"));


        //Step3:Update the new data

        existingResume.setTitle(updatedData.getTitle());
        existingResume.setThumbnailLink(updatedData.getThumbnailLink());
        existingResume.setTemplate(updatedData.getTemplate());
        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setContactInfo(updatedData.getContactInfo());
        existingResume.setWorkExperiences(updatedData.getWorkExperiences());
        existingResume.setEducations(updatedData.getEducations());
        existingResume.setSkills(updatedData.getSkills());
        existingResume.setProjects(updatedData.getProjects());
        existingResume.setCertifications(updatedData.getCertifications());
        existingResume.setLanguages(updatedData.getLanguages());
        existingResume.setInterests(updatedData.getInterests());

        //Step4:Update the details into database

        resumeRepository.save(existingResume);

        //Step5:return result
        return existingResume;
    }


    public void deleteResume(String resumeId, Object principal) {
        //Step1:get the current profile
        AuthResponse response = authService.getProfile(principal);

        //Step2:Call the repository finder method
        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(),resumeId)
                .orElseThrow(()-> new RuntimeException("Resume not found"));
        resumeRepository.delete(existingResume);


    }
}
