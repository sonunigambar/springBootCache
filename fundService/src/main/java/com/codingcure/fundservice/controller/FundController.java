package com.codingcure.fundservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingcure.fundservice.entity.Fund;
import com.codingcure.fundservice.service.FundService;


@RestController
@RequestMapping("/fund")
public class FundController {
	
	@Autowired
	FundService fundService;
	
	@Autowired
	private CacheManager cacheManager;
	
	
	@PostMapping("/createFund")
	public ResponseEntity<Fund> createFund(@RequestBody Fund fund) {
		Fund savedFund = fundService.createFund(fund);
		return new ResponseEntity<>(savedFund, HttpStatus.OK);
	}
	
	@GetMapping("/getFundById/{fundId}")
	public ResponseEntity<Fund> getFund(@PathVariable String fundId) {
		System.out.println(">>>>>fund service>>>");
		Fund savedFund = fundService.getFund(Integer.parseInt(fundId));
		return new ResponseEntity<>(savedFund, HttpStatus.OK);
	}
	
	@PutMapping("/updatefund")
	public ResponseEntity<Fund> updateFund(@RequestBody Fund fund){
		Fund updateFund = fundService.updatefund(fund);
		return new ResponseEntity<>(updateFund, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteFund/{fundId}")
	public ResponseEntity<String> deleteFund(@PathVariable String fundId) {
		String deletefund = fundService.deletefund(Integer.parseInt(fundId));
		return new ResponseEntity<>(deletefund, HttpStatus.OK);
	}
	
	@GetMapping("/getCache")
	public Cache getCacheInfo() {
		return cacheManager.getCache("fund");
	}


}
