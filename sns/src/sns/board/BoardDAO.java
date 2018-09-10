package sns.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sns.util.DBConn;

public class BoardDAO {
	Connection conn = DBConn.getConnection();
	
	// 게시물 추가
	public void insertBoard(BoardDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO board(num, id, content, imgName, place) values(board_seq.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getImgName());
			pstmt.setString(4, dto.getPlace());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
	}
	
	// 게시물 전체 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM board";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	
	// 사용자 별 게시물 개수
	public int dataCount(String id) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM board WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	
	// 전체 게시물 리스트
	public List<BoardDTO> listBoard(int start, int end){
		List<BoardDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM(");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM(");
			sb.append("			SELECT num, b.id, nick, content, imgName, created, place");
			sb.append("			FROM board b join member1 m1 on m1.id = b.id");
			sb.append("			ORDER BY num desc");
			sb.append("		)tb WHERE ROWNUM <=?");
			sb.append("	)WHERE rnum>=?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setNick(rs.getString("nick"));
				dto.setContent(rs.getString("content"));
				dto.setImgName(rs.getString("imgName"));
				dto.setCreated(rs.getString("created"));
				dto.setPlace(rs.getString("place"));
				
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}

	// 특정 사용자 게시물 리스트(프로필)
	public List<BoardDTO> listBoard(int start, int end, String id){
		List<BoardDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM(");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM(");
			sb.append("			SELECT num, b.id, nick, content, imgName, created, place");
			sb.append("			FROM board b join member1 m1 on m1.id = b.id ");
			sb.append("			WHERE b.id = ?");
			sb.append("			ORDER BY num desc");
			sb.append("		)tb WHERE ROWNUM <= ?");
			sb.append("	)WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setNick(rs.getString("nick"));
				dto.setContent(rs.getString("content"));
				dto.setImgName(rs.getString("imgName"));
				dto.setCreated(rs.getString("created"));
				dto.setPlace(rs.getString("place"));
				
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}
	
	// 검색 게시물 리스트
	public List<BoardDTO> listBoard(String searchValue, int start, int end){
		List<BoardDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM(");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM(");
			sb.append("			SELECT num, b.id, nick, content, imgName, created, place");
			sb.append("			FROM board b join member1 m1 on m1.id = b.id ");
			sb.append("			WHERE INSTR(content,'#'||?) > 0");
			sb.append("			ORDER BY num desc");
			sb.append("		)tb WHERE ROWNUM <= ?");
			sb.append("	)WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setNick(rs.getString("nick"));
				dto.setContent(rs.getString("content"));
				dto.setImgName(rs.getString("imgName"));
				dto.setCreated(rs.getString("created"));
				dto.setPlace(rs.getString("place"));
				
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}
	
	// 검색 게시물 COUNT
	public int searchDataCount(String searchValue) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM board WHERE INSTR(content,'#'||?)>0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	
	// 게시물 삭제
	public void deleteBoard(int num) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM board where num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
	}
	
	// 게시물 조회
	public BoardDTO readBoard(int num) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT num, b.id, nick, content, imgname, created, place");
			sb.append("		FROM board b JOIN member1 m on b.id=m.id");
			sb.append("		WHERE num = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setNick(rs.getString("nick"));
				dto.setContent(rs.getString("content"));
				dto.setImgName(rs.getString("imgName"));
				dto.setCreated(rs.getString("created"));
				dto.setPlace(rs.getString("place"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return dto;
	}
	
	// 게시물 수정
	public void updateBoard(BoardDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE board SET content=?, imgName=?, place=? WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getImgName());
			pstmt.setString(3, dto.getPlace());
			pstmt.setInt(4, dto.getNum());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
	}
	
	// 게시물 좋아요 추가
	public int insertBoardLike(int num, String id) {
		int result=0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO boardLike(num, id) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, id);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			try {
				pstmt.close();
				pstmt = null;
				sql = "DELETE FROM boardLike where num=? AND id=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				pstmt.setString(2, id);
				
				pstmt.executeUpdate();
			} catch (Exception e2) {
				System.out.println(e.toString());
			}
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		
		return result;
	}
	
	// 게시물 좋아요 count
	public int countBoardLike(int num) {
		int result = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM boardLike WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		
		return result;
	}
	
	// 댓글 등록
	public void insertReply(ReplyDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO boardReply(replyNum, num, id, content) values(boardReply_seq.nextval,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getId());
			pstmt.setString(3, dto.getContent());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
	}
	
	// 댓글 리스트(게시물에 따라)
	public List<ReplyDTO> listReply(int start, int end, int num){
		List<ReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("	SELECT ROWNUM rnum, tb.* FROM(");
			sb.append("			SELECT replyNum, r.num, r.id, nick, r.content, r.created");
			sb.append("			FROM boardReply r LEFT OUTER JOIN board b ON r.num = b.num");
			sb.append("			LEFT OUTER JOIN member1 m ON m.id = r.id");
			sb.append("			WHERE r.num= ? ");
			sb.append("		ORDER BY replyNum desc");
			sb.append("	)tb WHERE ROWNUM <= ?");
			sb.append(") WHERE rnum >= ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, num);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReplyDTO dto = new ReplyDTO();
				dto.setReplyNum(rs.getInt("replyNum"));
				dto.setNum(rs.getInt("num"));
				dto.setId(rs.getString("id"));
				dto.setNick(rs.getString("nick"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}
	// 댓글 갯수
	public int replyCount(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM boardReply WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
			} catch (Exception e2) {
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	
	// 댓글 삭제
	public int deleteReply(int replyNum, String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM boardReply WHERE replyNum = ? AND id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, replyNum);
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
	
	// 메시지 보내기
	public void sendMsg(MsgDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO msg(msgNum,sendId,getId,title,message) VALUES(msg_seq.NEXTVAL,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSendId());
			pstmt.setString(2, dto.getGetId());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getMessage());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e2) {
			}
		}
	}
}
