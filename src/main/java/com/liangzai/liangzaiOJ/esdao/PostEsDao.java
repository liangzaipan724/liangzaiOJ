package com.liangzai.liangzaiOJ.esdao;

import com.liangzai.liangzaiOJ.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liliangzai">程序员鱼皮</a>
 * @from <a href="https://liangzai.icu">编程导航知识星球</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    List<PostEsDTO> findByUserId(Long userId);
}