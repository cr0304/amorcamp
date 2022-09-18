package com.camper.controller;

import com.camper.entity.Camp;
import com.camper.service.CampService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    //캠핑장 가입승인해주는 관리자 페이지 불러오기
    private final CampService campService;

    @GetMapping(value = "/permit")
    public String permit(Model model){

        try{
            String permission = "WAIT"; //임의로 NULL값 변수를 만들어줌.
            List<Camp> camps = campService.getAllCamp(permission); //변수 permission를 들고 간다.
            model.addAttribute("camps",camps);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/permit";
        }


        return "/admin/permit";
    }


    @PostMapping(value = "/permit/ok/{campId}") //캠핑장 등록 허가해줄 경우
    public String OKPermit(@PathVariable("campId")Long campId, Model model){
        try{
            campService.updatePermission(campId,"OK");
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/permit";
        }
        return "redirect:/permit";
    }

    @PostMapping(value = "/permit/no/{campId}") //캠핑장 등록 거절할 경우
    public String NOPermit(@PathVariable("campId")Long campId,Model model){

        try{
            campService.updatePermission(campId,"NO");
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/permit";
        }
        return "redirect:/permit";
    }




}
