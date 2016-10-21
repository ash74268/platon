/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vorb.platon.persistence;

import de.vorb.platon.model.Comment;
import de.vorb.platon.model.CommentThread;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    @Query("select c from Comment c where c.thread = :thread and c.status in ('DELETED', 'PUBLIC') "
            + "order by c.creationDate asc")
    List<Comment> findByThread(@Param("thread") CommentThread thread);

    @Modifying
    @Query("update Comment c set c.status = :status where c.id = :commentId")
    void setStatus(@Param("commentId") Long commentId, @Param("status") Comment.Status status);

    @Query("select count(c) from Comment c where c.thread = :thread and c.status = 'PUBLIC'")
    Long countCommentsOfThread(@Param("thread") CommentThread thread);

}
