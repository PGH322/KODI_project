package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dto.ReadPostOneDTO;
import dto.WritePostDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import service.ModifyPostService;
import service.ReadPostOneService;

@Controller
@RequestMapping("/api")
public class ModifyPostController {
	
	@Autowired
	@Qualifier("readpostoneservice")
	ReadPostOneService oneservice;
	
	@Autowired
	@Qualifier("modifypostservice")
	ModifyPostService modifyservice;
	
	//게시물 수정 페이지
	@GetMapping("/post/modify/{postIdx}")
	public ModelAndView modifyPost(@PathVariable("postIdx") int postIdx, HttpSession session) {
		//게시물 하나에 대한 데이터
		ReadPostOneDTO readPostOne = oneservice.getReadPostOne(postIdx);
		
		ModelAndView mv = new ModelAndView();
		
		if(session.getAttribute("memberIdx") == null) {
        	mv.addObject("isSession", false);
		} else {
        	mv.addObject("isSession", true);
		}
		
		mv.addObject("readPostOne", readPostOne);
		mv.setViewName("Editpost");
		
		return mv;
	}
	
	//작성완료 클릭
	@PostMapping("/post/isupdate")
	public String isUpdate(WritePostDTO writePostDTO, 
			HttpSession session,
			HttpServletRequest request) 
			throws IllegalStateException, IOException {
		//세션 받아서 int 타입으로 변환
		String sessionIdx = (String)session.getAttribute("memberIdx");
		Integer myMemberIdx = Integer.parseInt(sessionIdx);
		
		//이미지 파일 이름 저장
		List<String> fileName = new ArrayList<String>();
		//받아온 파일들 저장
		MultipartFile file[] = writePostDTO.getImagePost();
		//이미지 파일들 로컬에 저장
		String fileDir = "C:/FullStack/파이널 프로젝트/git/KODI_project/KODI/src/main/resources/static/image/db/";
		String imagePath = "";
		
//		File dirFile = new File(fileDir);
//		File[] fileList = dirFile.listFiles();
//		System.out.println(fileList.toString());
		
		//파일이 있는 경우에만 저장(사진첨부를 눌러서 파일 선택이 생성된 경우)
		if(file != null) {
			for(MultipartFile data : file) {
				imagePath = fileDir + data.getOriginalFilename();
				//파일 선택에 파일이 들어가 있는 경우
				if(!data.getOriginalFilename().equals("")) {
					fileName.add(data.getOriginalFilename());
					data.transferTo(new File(imagePath));
				}
			}
		}
		modifyservice.updatePost(writePostDTO, fileName, myMemberIdx);
		
		//상세 게시글로 돌아가기 위한 게시글 idx
		int postIdx = writePostDTO.getPostIdx();
		
		return "redirect:/api/post/" + postIdx;
	}
	
	//현재 DB에 있는 이미지 삭제
	@PostMapping("/post/image/isdelete")
	public void isDeleteImage(
			@RequestParam("imageSrc") String imageSrc,
			@RequestParam("postIdx") int postIdx) {
		
		modifyservice.deleteImageSrc(postIdx, imageSrc);
		
	}
	
}
