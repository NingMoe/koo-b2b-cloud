package com.koolearn.cloud.exam.examcore.paper.service;

import java.util.List;

import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBar;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;

public interface TestPaperStructureService {

	List<TestPaperStructure> findQuestionIdsByPaperId(int paperId);

	List<TestPaperStructure> findItemsByPaperId(int paperId);

    /**
     * 获取模版数据，组卷过程中
     * @param filter
     * @return
     */
    QuestionBar findPaperBar(PaperPager filter);
}
