package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.persistence.SearchHistoryRepository;

@Service
public class SearchHistoryService {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    public void insertHistoryItem(int contentId, Long no_data) {
        if (!searchHistoryRepository.existsByGocampingContentIdAndMemberNo_data(contentId, no_data)) {
            searchHistoryRepository.insertHistoryItem(contentId, no_data);
        }
    }
}
