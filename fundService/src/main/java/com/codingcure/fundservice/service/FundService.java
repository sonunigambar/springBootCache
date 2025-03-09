package com.codingcure.fundservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.codingcure.fundservice.entity.Fund;
import com.codingcure.fundservice.repositary.FundRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
public class FundService {
	
	@Autowired
	FundRepo fundRepo;
	
	@Autowired
	RestTemplate restTemplate;

	@CircuitBreaker(name = "pricingService", fallbackMethod = "getDefaultPrice")
	public Fund createFund(Fund fund) {
		try {
			System.out.println("create fund method call>>>>>");
			String url = "http://localhost:9094/getPrice/123";
			Double price = restTemplate.getForObject(url, Double.class);
			fund.setNav(price);
		} catch (RestClientException e) {
			throw new RuntimeException("pricing service is not avaialble", e);
		}
		 return fundRepo.save(fund);
		
	}
	
//	fallback method
	private Fund getDefaultPrice(Fund fund, Throwable t){
		System.out.println("Fall back method called: >>>"+t.getMessage());
		fund.setNav(135.98);
		return fundRepo.save(fund);
	}
	

	@Cacheable(value = "fund", key = "#fundId", condition = "#fundId > 1")
	public Fund getFund(int fundId) {
		Fund fundInfo = null;
		 Optional<Fund> fund = fundRepo.findById(fundId);
		 if(fund.isPresent()) {
			fundInfo = fund.get();
		 }
		 return fundInfo;
	}

	@CachePut(value = "fund", key = "#fund.fundId")
	public Fund updatefund(Fund fund) {
		Fund updatedFund = fundRepo.save(fund);
		return updatedFund;
	}

	@CacheEvict(value = "fund", key = "#fundId" )
	public String deletefund(int fundId) {
		String deletedMsg = null;
		Optional<Fund> existingFund = fundRepo.findById(fundId);
		if (existingFund.isPresent()) {
			fundRepo.deleteById(fundId);
			deletedMsg = "Deleted successfully of Id: "+fundId;
		}else {
			deletedMsg = "fund id is not available";
		}
		return deletedMsg;
	}
	
//	@Scheduled(fixedRate = 20000)
//	@CacheEvict(value = "fund", allEntries = true)
//	public void clearFundCache() {
//		System.out.println("Fund cache has cleared now");
//		
//	}
	
}
