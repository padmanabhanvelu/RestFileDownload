package com.download.rest;

/**
 * @author Padmanabhan
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDownloadResource {
    	
	@GetMapping("/")
	public String welcome() {
		System.out.println("Welcome to India");
		return "OK";
	}
	

	/**
	 * Download as simple text file
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public void getDownload(HttpServletResponse response) throws IOException {

		// Get your file stream from wherever.
		File initialFile = new File("C://temp/GSSM3065.log");
	    //InputStream targetStream = new FileInputStream(initialFile);
		InputStream myStream = new FileInputStream(initialFile);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=myfilename.txt");
		response.setContentType("txt/plain");

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStream, response.getOutputStream());
		response.flushBuffer();
	}
	
	/**
	 * Download as a stream
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/pdf/{fileName:.+}", method = RequestMethod.GET, produces = "application/pdf")
	 public ResponseEntity<InputStreamResource> download(@PathVariable("fileName") String fileName) throws IOException {
	  System.out.println("Calling Download:- " + fileName);
	  ClassPathResource pdfFile = new ClassPathResource("downloads/" + fileName);
	  HttpHeaders headers = new HttpHeaders();
	  headers.setContentType(MediaType.parseMediaType("application/pdf"));
	  headers.add("Access-Control-Allow-Origin", "*");
	  headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
	  headers.add("Access-Control-Allow-Headers", "Content-Type");
	  headers.add("Content-Disposition", "filename=" + fileName);
	  headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	  headers.add("Pragma", "no-cache");
	  headers.add("Expires", "0");

	  headers.setContentLength(pdfFile.contentLength());
	  ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
	    new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
	  return response;

	 }
}