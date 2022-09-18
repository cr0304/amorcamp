package com.camper.service;

import com.camper.entity.Url;
import com.camper.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public void updateUrl(String url,Long id){
        Url checkUrl = urlRepository.findByUrl(id);
        checkUrl.updateUrl(url); //URL 변경
    }
}

