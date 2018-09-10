package sns.board;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.sf.json.JSONObject;
import sns.member.MemberDAO;
import sns.member.MemberDTO;
import sns.member.SessionInfo;
import sns.util.FileManager;
import sns.util.MyServlet;
import sns.util.MyUtil;

@WebServlet("/board/*")
public class BoardServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String header = req.getHeader("AJAX");
		if(header != null && header.equals("true") && info == null) {
			resp.sendError(403);
			return;
		}
		
		if(info == null) {
			resp.sendRedirect(cp+"/mem/login.do");
			return;
		}
		
		// 파일 업로드 경로 지정
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"photo";
		File f = new File(pathname);
		// 경로가 존재하지 않으면 만든다
		if(! f.exists()) {
			f.mkdirs();
		}
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("main.do") != -1) {
			main(req, resp);
		} else if(uri.indexOf("created.do") != -1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if(uri.indexOf("profile.do") != -1) {
			profileMain(req,resp);
		} else if(uri.indexOf("profileList.do") != -1) {
			profileList(req,resp);
		} else if(uri.indexOf("insertBoardLike.do") != -1) {
			insertBoardLike(req,resp);
		} else if(uri.indexOf("countBoardLike.do") != -1) {
			countBoardLike(req,resp);
		} else if(uri.indexOf("insertReply.do") != -1) {
			insertReply(req,resp);
		} else if(uri.indexOf("listReply.do") != -1) {
			listReply(req,resp);
		} else if(uri.indexOf("deleteReply.do") != -1) {
			deleteReply(req,resp);
		} else if(uri.indexOf("searchList.do") != -1) {
			searchList(req,resp);
		} else if(uri.indexOf("msg.do") != -1) {
			msgForm(req,resp);
		} else if(uri.indexOf("msg_ok.do") != -1) {
			msgSubmit(req,resp);
		} 
	}


	private void msgForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String getId = req.getParameter("getId");
		String sendId = req.getParameter("sendId");
		
		req.setAttribute("getId", getId);
		req.setAttribute("sendId", sendId);
		forward(req, resp, "/WEB-INF/views/board/msgForm.jsp");
		
	}
	
	private void msgSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		MsgDTO dto = new MsgDTO();
		
		dto.setSendId(req.getParameter("sendId"));
		System.out.println(req.getParameter("sendId"));
		dto.setGetId(req.getParameter("getId"));
		dto.setTitle(req.getParameter("title"));
		dto.setMessage(req.getParameter("message"));
		
		dao.sendMsg(dto);
		
		resp.sendRedirect(cp+"/board/main.do");
		
	}

	private void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 등록
		BoardDAO dao = new BoardDAO();
		ReplyDTO dto = new ReplyDTO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		// list.jsp의 댓글 등록 부분에서 받은 num
		int num = Integer.parseInt(req.getParameter("num"));
		dto.setNum(num);
		// id는 세션에 저장된 정보에서 얻음
		dto.setId(info.getUserId());
		dto.setContent(req.getParameter("content"));
		
		String state = "false";
		// 추가가 성공하면
		dao.insertReply(dto);
		// state를 true로 변경한다
		state = "true";
		
		//JSON 형태로 state를  list.jsp로 전달한다.
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}

	private void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 목록
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		// list.jsp의 댓글 리스트 함수에서 받아온 num, pageNo
		int num = Integer.parseInt(req.getParameter("num"));
		String pageNo = req.getParameter("pageNo");
		// 페이지 1로 초기화
		int current_page = 1;
		// pageNo가 null이 아니면
		if(pageNo != null)
			// pageNo를 정수로 변경해서 current_page에 대입
			current_page = Integer.parseInt(pageNo);
		
		// 한번에 띄울 댓글 개수
		int replyPerPage = 5;
		// 전체 댓글 개수
		int replyCount = dao.replyCount(num);
		// 총 페이지 계산
		int total_page = util.pageCount(replyPerPage, replyCount);
		
		// 현재 페이지가 전체 페이지보다 크면 전체 페이지값을 현재 페이지 값에 대입
		if(current_page > total_page)
			current_page = total_page;
		
		// 리스트를 5개씩 자르기 위해 start, end 설정
		int start = (current_page-1)*replyPerPage+1;
		int end = (current_page)*replyPerPage;
		
		// 조건에 맞는 댓글 리스트
		List<ReplyDTO> replyList = dao.listReply(start, end, num);
		
		// 띄어쓰기, 엔터, 태그처리 등 잠재적 문제점 제거
		for(ReplyDTO dto : replyList)
			dto.setContent(util.htmlSymbols(dto.getContent()));
		
		// listReplyPage라는 function을 활용해 paging
		String paging = util.pagingMethod(current_page, total_page, "listReplyPage");
		
		// 포워딩할 데이터
		req.setAttribute("paging", paging);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyList", replyList);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		
		// listReply.jsp로 포워딩
		forward(req, resp, "/WEB-INF/views/board/listReply.jsp");
		
		
	}
	
	private void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 댓글 삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		BoardDAO dao = new BoardDAO();
		
		// 삭제 버튼의 data-replyNum에서 받아온 replyNum
		int replyNum = Integer.parseInt(req.getParameter("replyNum"));
		String state = "false";
		// 삭제된 행 수를 리턴 받아서
		int result = dao.deleteReply(replyNum, info.getUserId());
		// 1개 행이 삭제되었으면(정상처리이면)
		if(result == 1)
			// state는 true로 변경
			state = "true";
		
		// JSON 형태로 state 전송
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());	
	}


	private void insertBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 좋아요 추가
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		BoardDAO dao = new BoardDAO();
		
		String state = "false";
		// 버튼이 가지고 있는 num값을 받아옴
		int num = Integer.parseInt(req.getParameter("num"));
		// 추가된 행의 개수 리턴 받음
		int result = dao.insertBoardLike(num, info.getUserId());
		// 1개 행이 추가 되었다면 true
		if(result == 1) {
			state = "true";
		// 0개 행이 추가 되었다면 false
		} else if(result == 0) {
			state = "false";
		}
		// JSON 형태로 state 데이터 전송
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	private void countBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 좋아요 개수 띄우기
		BoardDAO dao = new BoardDAO();
		// countBoardLike라는 이름의 function의 param으로 받아온 num
		int num = Integer.parseInt(req.getParameter("num"));
		// 좋아요 개수 받아서
		int countBoardLike = dao.countBoardLike(num);
		
		// JSON 형태로 데이터 전송(list.jsp의 function)
		JSONObject job = new JSONObject();
		job.put("countBoardLike", countBoardLike);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}

	private void profileMain(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 개인 프로필 화면
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		// 주소값 or 리스트의 아이디값을 통해 id 받아옴
		String id = req.getParameter("id");
		String fanId = info.getUserId();
		// id로 해당하는 정보를 읽는다.
		MemberDTO dto = dao.readMember(id);
		// 없는 dto면 시작화면으로 이동
		if(dto == null) {
			resp.sendRedirect(cp+"/board/main.do");
			return;
		}
		// follow 버튼 처리를 위한 체크
		String following = "false";
		int result = dao.readFollow(fanId, id);
		if(result == 1) {
			following = "true";
		} else if(result == 0) {
			following = "false";
		}
		
		// 그게 아니면 profile.jsp로 포워딩
		req.setAttribute("dto", dto);
		req.setAttribute("following",following);
		forward(req,resp,"/WEB-INF/views/board/profile.jsp");
	}
	
	private void profileList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 개인 프로필 게시물 목록
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		// profile.jsp에서 id, page값 받아옴
		String page = req.getParameter("pageNo");
		String id = req.getParameter("id");

		int current = 1;
		if(page != null)
			current = Integer.parseInt(page);
		
		int rows = 8;
		// 데이터 카운트 유저 기준으로 잡아야함
		int dataCount = dao.dataCount(id);
		int total_page = util.pageCount(rows, dataCount);
		
		if(current>total_page)
			current=total_page;
		
		int start = (current-1)*rows+1;
		int end = current*rows;
		
		// DAO 불러오기
		List<BoardDTO> list = dao.listBoard(start, end, id);
		for(BoardDTO dto : list) {
			dto.setContent(util.htmlSymbols(dto.getContent()));
		}
		
		String articleUrl = cp+"/board/article.do?page="+current;
		
		String paging = util.paging(current, total_page);
		
		// 포워딩할 데이터
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("list", list);
		req.setAttribute("total_page", total_page);
		req.setAttribute("current", current);
		req.setAttribute("articleUrl", articleUrl);
		
		// pfList.jsp로 포워딩
		forward(req,resp,"/WEB-INF/views/board/pfList.jsp");
	}
	
	private void main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		// 전체 리스트 화면으로 이동
		forward(req,resp,"/WEB-INF/views/board/list.jsp");
		
	}

	// 전체 리스트
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		// function listPage(page) 에서 받아온 값
		String page = req.getParameter("pageNo");
		
		int current = 1;
		if(page != null)
			current = Integer.parseInt(page);
		
		// 한 페이지에 보여줄 게시물 수
		int rows = 3;
		// 전체 게시물 수
		int dataCount = dao.dataCount();
		// 전체 페이지 수
		int total_page = util.pageCount(rows, dataCount);
		
		if(current>total_page)
			current=total_page;
		
		int start = (current-1)*rows+1;
		int end = current*rows;
		
		List<BoardDTO>list = dao.listBoard(start, end);
		for(BoardDTO dto : list) {
			dto.setContent(util.htmlSymbols(dto.getContent()));
		}
		String paging = util.paging(current, total_page);
		
		JSONObject job = new JSONObject();
		
		// JSON 형식으로 전송할 데이터 => list.jsp의 리스트 구현 부분으로 전송함
		job.put("list", list);
		job.put("paging", paging);
		job.put("dataCount", dataCount);
		job.put("total_page", total_page);
		job.put("pageNo", current);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
	
	private void searchList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("pageNo");
		System.out.println(page);
		String searchValue = req.getParameter("searchValue");
		System.out.println(searchValue);
		
		int current_page = 1;
		if(page != null)
			current_page = Integer.parseInt(page);
		
		int rows = 3;
		int searchCount = dao.searchDataCount(searchValue);
		int total_page = util.pageCount(rows, searchCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		int start = (current_page-1)*rows+1;
		int end = (current_page)*rows;
		
		List<BoardDTO> searchList = dao.listBoard(searchValue, start, end);
		
		for(BoardDTO dto : searchList) {
			dto.setContent(util.htmlSymbols(dto.getContent()));
		}
		
		String paging = util.pagingMethod(current_page, total_page, "searchListPage");
		
		JSONObject job = new JSONObject();
		
		// JSON 형식으로 전송할 데이터 => list.jsp의 리스트 구현 부분으로 전송함
		job.put("searchList", searchList);
		job.put("paging", paging);
		job.put("searchCount", searchCount);
		job.put("total_page", total_page);
		job.put("pageNo", current_page);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 작성화면으로 이동
		
		// 이동해온 화면이 list인지 profile인지 구분하기 위한 screen값. 헤더에서 넘어온다.
		String screen = req.getParameter("screen");
		
		req.setAttribute("screen", screen);
		// 추가 or 수정을 결정하기 위한 mode => created로 지정한다.
		req.setAttribute("mode", "created");
		// created.jsp로 포워딩 => 게시물 추가 화면 역할
		forward(req, resp, "/WEB-INF/views/board/created.jsp");
	}
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 추가
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		// 파일 업로드를 위한 변수들 선언
		String encType = "utf-8";
		// 최대 파일 크기
		int maxSize = 5*1024*1024;
		
		// 파일 업로드를 위해 MultipartRequest 사용
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		String screen = mreq.getParameter("screen");
		
		if(mreq.getFile("upload") != null) {
			dto.setId(info.getUserId());
			dto.setContent(mreq.getParameter("content"));
			dto.setPlace(mreq.getParameter("p_input"));
			
			// 업로드 창에 올라온 파일명
			String saveName = mreq.getFilesystemName("upload");
			// 업로드된 시간으로 이름 변경해주는 method
			saveName = FileManager.doFilerename(pathname, saveName);
			
			dto.setImgName(saveName);
			// 데이터 추가
			dao.insertBoard(dto);
			// 이동해온 페이지에 따라 이동할 페이지 결정
			if(screen.equals("list")) {
				resp.sendRedirect(cp+"/board/main.do");				
			} else if(screen.equals("profile")) {
				resp.sendRedirect(cp+"/board/profile.do?id="+info.getUserId());	
			}
		}
		
	}
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// profile에서 게시물 조회(1개씩)
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		// 이미지의 data-num 속성이 가지고 있던 게시물 번호 받아옴
		int num = Integer.parseInt(req.getParameter("num"));
		
		// 받아온 게시물 번호로 조회
		BoardDTO dto = dao.readBoard(num);
		dto.setContent(util.htmlSymbols(dto.getContent()));
		
		// dto와 함께 포워딩
		req.setAttribute("dto", dto);
		forward(req, resp, "/WEB-INF/views/board/article.jsp");
	}
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 수정 화면으로 이동
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		// 버튼이 가지고있는 속성값에서 받아온 num
		int num = Integer.parseInt(req.getParameter("num"));
		// 이전 페이지 결정
		String screen = req.getParameter("screen");
		
		// 받아온 번호로 데이터 읽음
		BoardDTO dto = dao.readBoard(num);
		if(dto == null) {
			resp.sendRedirect(cp+"/board/main.do");
			return;
		}
		
		// 글 작성자와 현재 사용자가 다르다면
		if(! info.getUserId().equals(dto.getId())) {
			resp.sendRedirect(cp+"/board/main.do");
			return;
		}
		
		// mode는 게시물 수정 + screen과 dto를 포함해 created.jsp로 포워딩
		req.setAttribute("mode", "update");
		req.setAttribute("screen", screen);
		req.setAttribute("dto", dto);
		forward(req, resp, "/WEB-INF/views/board/created.jsp");
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 수정
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		String encType = "utf-8";
		// 파일 최대 크기
		int maxSize = 5*1024*1024;
		
		// 파일 업로드를 위해 MultipartRequest 사용
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		
		BoardDTO dto = new BoardDTO();
		
		String screen = mreq.getParameter("screen");
		dto.setNum(Integer.parseInt(mreq.getParameter("num")));
		dto.setContent(mreq.getParameter("content"));
		dto.setPlace(mreq.getParameter("p_input"));
		
		String imgName = mreq.getParameter("imgName");
		if(mreq.getFile("upload") != null) {
			FileManager.doFiledelete(pathname, imgName);
			
			// 파일 선택 창에서 받아온 이름
			String saveFilename = mreq.getFilesystemName("upload");
			// 파일 이름 재설정
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setImgName(saveFilename);
		} else {
			// 파일 수정 안한 경우
			dto.setImgName(imgName);
		}
		// 게시물 수정
		dao.updateBoard(dto);
		
		// screen 값에 따라 이동 할 페이지 나눠짐
		if(screen.equals("list")) {
			resp.sendRedirect(cp+"/board/main.do");			
		} else if(screen.equals("profile")) {
			resp.sendRedirect(cp+"/board/profile.do?id="+info.getUserId());		
		}
		
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 삭제
		BoardDAO dao = new BoardDAO();
		// 버튼에서 받아온 num
		int num = Integer.parseInt(req.getParameter("num"));
		// 삭제 성공하면
		String state = "false";
		String screen = req.getParameter("screen");
		dao.deleteBoard(num);
		// state는 true로 변경
		state = "true";
		
		// JSON 형태로 데이터 전송
		JSONObject job = new JSONObject();
		job.put("state", state);
		job.put("screen", screen);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

}
