package com.sjy.milestone.board.mapper;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.board.dto.DetailBoardDTO;
import com.sjy.milestone.board.dto.MenuBoardDTO;
import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.comment.mapper.CommentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface BoardMapper {

    @Mapping(target = "title", source = "detailBoardDTO.title")
    @Mapping(target = "content", source = "detailBoardDTO.content")
    @Mapping(target = "member", source = "member")
    Board toEntity(DetailBoardDTO detailBoardDTO, Member member);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "authorEmail", source = "member.userEmail")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "viewCount", source = "viewCount")
    @Mapping(target = "comments", source = "comments")
    DetailBoardDTO toDetailBoardDTO(Board board);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "totalView", source = "viewCount")
    MenuBoardDTO toMenuBoardDTO(Board board);
}