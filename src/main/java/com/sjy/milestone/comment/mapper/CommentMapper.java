package com.sjy.milestone.comment.mapper;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.comment.dto.CommentDTO;
import com.sjy.milestone.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", source = "commentDTO.commentId")
    @Mapping(target = "content", source = "commentDTO.content")
    @Mapping(target = "board", source = "board")
    @Mapping(target = "member", source = "member")
    @Mapping(target = "parentComment", source = "parentComment")
    Comment toEntity(CommentDTO commentDTO, Board board, Member member, Comment parentComment);

    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "authorEmail", source = "member.userEmail")
    @Mapping(target = "parentCommentId", source = "parentComment.id")
    @Mapping(target = "boardId", source = "board.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "childComments", source = "childComments")  // 재귀적으로 자식 댓글 처리
    CommentDTO toDTO(Comment comment);
}
