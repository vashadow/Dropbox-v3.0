package in.adityaparmar.server.controller;


import in.adityaparmar.server.entity.Activity;
import in.adityaparmar.server.entity.Content;
import in.adityaparmar.server.entity.Mapping;
import in.adityaparmar.server.entity.User;
import in.adityaparmar.server.entity.request.Delete;
import in.adityaparmar.server.entity.request.Folder;
import in.adityaparmar.server.entity.request.General;
import in.adityaparmar.server.entity.request.Share;
import in.adityaparmar.server.entity.response.ContentLoadResponse;
import in.adityaparmar.server.entity.response.Contents;
import in.adityaparmar.server.entity.response.Response;
import in.adityaparmar.server.entity.response.RootResponse;
import in.adityaparmar.server.service.ContentService;
import in.adityaparmar.server.service.MappingService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController    // This means that this class is a Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/content") // This means URL's start with /demo (after Application path)
public class ContentController  {

    @Autowired
    private ContentService contentService;

    @Autowired
    private MappingService mappingService;


    private static String UPLOADED_FOLDER = System.getProperty("user.dir")+ "/src/main/resources/static/";


    @RequestMapping(path="/load",method = RequestMethod.POST)
    public ContentLoadResponse getFolderData(@RequestBody Folder folder) {
        // This returns a JSON with the users
        return contentService.getFolderData(folder);
    }

    @RequestMapping(path="/root",method = RequestMethod.POST)
    public RootResponse getRoot(@RequestBody User user) {
        // This returns a JSON with the users
        return contentService.getRoot(user);
    }
    @RequestMapping(path="/createfolder",method = RequestMethod.POST)
    public ContentLoadResponse CreateFolder(@RequestBody Folder folder) {
        // This returns a JSON with the users
        return contentService.CreateFolder(folder);
    }

    @RequestMapping(path="/upload",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ContentLoadResponse fileupload(@RequestParam("file") MultipartFile multipartFile,
                                          @RequestParam("fileparent") String fileparent,
                                          @RequestParam("userid") String userid){

       // String email = (String) session.getAttribute("email");

        Content content = new Content();
        Date date = new Date();
        String virtualname= date+"_"+ multipartFile.getOriginalFilename();
        String filepath = UPLOADED_FOLDER + virtualname;
        Response response = new Response();
        ContentLoadResponse contentLoadResponse = new ContentLoadResponse();
        try {



            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(filepath);
            Files.write(path, bytes);

            contentLoadResponse = contentService.UploadFile(multipartFile.getOriginalFilename(),
                    virtualname,Integer.parseInt(fileparent),Integer.parseInt(userid));


        } catch (IOException e) {
            response.setStatus("error");
            response.setMsg("Error in uploading, Please Try Again.");
            contentLoadResponse.setContents(null);
            contentLoadResponse.setResponse(response);
        }


        return contentLoadResponse;
        //return new ResponseEntity<com.cmpe273.dropbox.backend.entity.Files>(newFile, HttpStatus.OK);
    }


    @RequestMapping(path="/share",method = RequestMethod.POST)
    public Response Share(@RequestBody Share share) {
        // This returns a JSON with the users
        return contentService.Share(share);
    }

    @RequestMapping(path="/removemember",method = RequestMethod.POST)
    public Response RemoveMember(@RequestBody General general) {
        // This returns a JSON with the users

        return mappingService.RemoveMember(Integer.parseInt(general.getData1()),Integer.parseInt(general.getData2()));
    }

    @RequestMapping(path="/delete",method = RequestMethod.POST)
    public Response DeleteFile(@RequestBody Delete data) {
        // This returns a JSON with the users

        return contentService.DeleteFile(data);
    }
    @RequestMapping(path="/dostar",method = RequestMethod.POST)
    public Response dostar(@RequestBody Contents data) {
        // This returns a JSON with the users

        return contentService.dostar(data);
    }


}