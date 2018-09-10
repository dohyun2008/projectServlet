package sns.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sns.util.DBConn;

public class MemberDAO {
	Connection conn = DBConn.getConnection();
	
	// 사용자 추가
	public void insertMember(MemberDTO dto) {
		PreparedStatement pstmt = null;
		String sql = "";
		
		try {
			sql = "INSERT INTO member1(id, pw, name, nick, pic) values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPw());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getNick());
			pstmt.setString(5, dto.getPic());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO member2(id, birth, email, tel) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getTel());
			
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
	
	// 사용자 정보 조회
	public MemberDTO readMember(String id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("select m1.id, pw, name, nick, pic, regDate, birth, email, tel ");
			sb.append("from member1 m1 left outer join member2 m2 on m1.id=m2.id ");
			sb.append("WHERE m1.id=?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setName(rs.getString("name"));
				dto.setNick(rs.getString("nick"));
				dto.setPic(rs.getString("pic"));
				dto.setRegDate(rs.getString("regDate"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
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
					rs.close();
			} catch (Exception e2) {
			}
		}
		return dto;
	}

	// 닉네임으로 사용자 정보 조회
	public MemberDTO readNick(String nick) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("SELECT m1.id, pw, name, nick, pic, regDate, birth, email, tel ");
			sb.append("FROM member1 m1 LEFT OUTER JOIN member2 m2 on m1.id=m2.id ");
			sb.append("WHERE nick=?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, nick);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setName(rs.getString("name"));
				dto.setNick(rs.getString("nick"));
				dto.setPic(rs.getString("pic"));
				dto.setRegDate(rs.getString("regDate"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
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
					rs.close();
			} catch (Exception e2) {
			}
		}
		return dto;
	}

	// 사용자 정보 수정
	public void updateMember(MemberDTO dto) {
		PreparedStatement pstmt=null;
		String sql = "";
		
		try {
			sql = "UPDATE member1 set pw=?, nick=?, pic=? where id=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getPw());
			pstmt.setString(2, dto.getNick());
			pstmt.setString(3, dto.getPic());
			pstmt.setString(4, dto.getId());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "UPDATE member2 set email=?, tel=? where id=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getId());
			
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
	
	// 사용자 탈퇴
	public void deleteMember(String id) {
		PreparedStatement pstmt=null;
		String sql = "";
		
		try {
			sql = "DELETE FROM member1 where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "DELETE FROM member2 where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
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
	
	// 팔로우 하기
	public int follow(String fan, String star) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "";

		try {
			sql = "INSERT INTO follow(fanId, starId) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, fan);
			pstmt.setString(2, star);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			// 팔로우 취소 처리
			//e.printStackTrace();
			try {
				pstmt.close();
				pstmt = null;
				sql = "DELETE FROM FOLLOW WHERE fanId=? and starId=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, fan);
				pstmt.setString(2, star);
				
				pstmt.executeUpdate();
				
			} catch (Exception e2) {
				//e.printStackTrace();
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
	
	public int readFollow(String fanId, String starId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "";
		
		try {
			sql = "SELECT COUNT(*) FROM follow WHERE fanId=? AND starId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, fanId);
			pstmt.setString(2, starId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
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
	
}
