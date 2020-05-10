/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java.example.myvideo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import com.java.example.myvideo.encoder.Encoder;
import com.java.example.myvideo.model.Video;
import com.java.example.myvideo.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 * @author Greg Turnquist
 */
@RestController
@CrossOrigin
public class VideoController {

	private static final Logger log = LoggerFactory.getLogger(VideoController.class);
	private static final String VIDEOS_FOLDER_PATH = "E:\\myvideo\\videos";

	private final VideoRepository repository;
	private final Encoder encoder = new Encoder();

	VideoController(VideoRepository repository) {
		this.repository = repository;
	}

	/**
	 * Look up all students, and transform them into a REST collection resource.
	 * Then return them through Spring Web's {@link ResponseEntity} fluent API.
	 */
	/*@GetMapping("/video")
	ResponseEntity<CollectionModel<EntityModel<Video>>> findAll() {
		//log.info("findAll");
		List<EntityModel<Video>> students = StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(video -> new EntityModel<>(video, //
						linkTo(methodOn(VideoController.class).findOne(video.getId())).withSelfRel(), //
						linkTo(methodOn(VideoController.class).findAll()).withRel("video"))) //
				.collect(Collectors.toList());

		return ResponseEntity.ok( //
				new CollectionModel<>(students, //
						linkTo(methodOn(VideoController.class).findAll()).withSelfRel()));
	}*/

	@PostMapping("/upload")
	ResponseEntity<?> newVideo(@RequestBody MultipartFile rawFile) {
		//log.info("newVideo");
		try {
			String rawName = rawFile.getOriginalFilename();
			assert rawName != null;
			File filepath = new File(VIDEOS_FOLDER_PATH, rawName);
			rawFile.transferTo(filepath);
			int fileId = Integer.parseInt(rawName.split("\\.",2)[0]);
			String fileName = rawFile.getName();
			Video video = new Video(fileId, fileName);
			String srcPath = filepath.getPath();
			String dstPath360 = srcPath.substring(0, srcPath.lastIndexOf('.')) + "360.mp4";
			String dstPath720 = srcPath.substring(0, srcPath.lastIndexOf('.')) + "720.mp4";
			video.setPath360(dstPath360);
			video.setPath720(dstPath720);
			repository.save(video);
			encoder.encode(srcPath, dstPath360, "480x360");
			encoder.encode(srcPath, dstPath720, "1080x720");
			return ResponseEntity.ok("");
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Unable to create " + rawFile);
		}
	}

	/**
	 * Look up a single {@link Video} and transform it into a REST resource. Then
	 * return it through Spring Web's {@link ResponseEntity} fluent API.
	 *
	 * @param id
	 */
	@GetMapping("/view/{id}/360")
	ResponseEntity<EntityModel<String>> view360(@PathVariable long id) {

		if(repository.findById(id).isPresent()) {
			String tmpStr = repository.findById(id).get().getPath360();
			return ResponseEntity.ok(new EntityModel<>(tmpStr));
		}
		return ResponseEntity.badRequest().body(new EntityModel<>("Unable"));
	}

	@GetMapping("/view/{id}/720")
	ResponseEntity<EntityModel<String>> view720(@PathVariable long id) {

		if(repository.findById(id).isPresent()) {
			String tmpStr = repository.findById(id).get().getPath720();
			return ResponseEntity.ok(new EntityModel<>(tmpStr));
		}
		return ResponseEntity.badRequest().body(new EntityModel<>("Unable"));
	}

}
