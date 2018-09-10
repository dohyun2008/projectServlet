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
		
		// ���� ���ε� ��� ����
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"photo";
		File f = new File(pathname);
		// ��ΰ� �������� ������ �����
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
		// ��� ���
		BoardDAO dao = new BoardDAO();
		ReplyDTO dto = new ReplyDTO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		// list.jsp�� ��� ��� �κп��� ���� num
		int num = Integer.parseInt(req.getParameter("num"));
		dto.setNum(num);
		// id�� ���ǿ� ����� �������� ����
		dto.setId(info.getUserId());
		dto.setContent(req.getParameter("content"));
		
		String state = "false";
		// �߰��� �����ϸ�
		dao.insertReply(dto);
		// state�� true�� �����Ѵ�
		state = "true";
		
		//JSON ���·� state��  list.jsp�� �����Ѵ�.
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}

	private void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ��� ���
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		// list.jsp�� ��� ����Ʈ �Լ����� �޾ƿ� num, pageNo
		int num = Integer.parseInt(req.getParameter("num"));
		String pageNo = req.getParameter("pageNo");
		// ������ 1�� �ʱ�ȭ
		int current_page = 1;
		// pageNo�� null�� �ƴϸ�
		if(pageNo != null)
			// pageNo�� ������ �����ؼ� current_page�� ����
			current_page = Integer.parseInt(pageNo);
		
		// �ѹ��� ��� ��� ����
		int replyPerPage = 5;
		// ��ü ��� ����
		int replyCount = dao.replyCount(num);
		// �� ������ ���
		int total_page = util.pageCount(replyPerPage, replyCount);
		
		// ���� �������� ��ü ���������� ũ�� ��ü ���������� ���� ������ ���� ����
		if(current_page > total_page)
			current_page = total_page;
		
		// ����Ʈ�� 5���� �ڸ��� ���� start, end ����
		int start = (current_page-1)*replyPerPage+1;
		int end = (current_page)*replyPerPage;
		
		// ���ǿ� �´� ��� ����Ʈ
		List<ReplyDTO> replyList = dao.listReply(start, end, num);
		
		// ����, ����, �±�ó�� �� ������ ������ ����
		for(ReplyDTO dto : replyList)
			dto.setContent(util.htmlSymbols(dto.getContent()));
		
		// listReplyPage��� function�� Ȱ���� paging
		String paging = util.pagingMethod(current_page, total_page, "listReplyPage");
		
		// �������� ������
		req.setAttribute("paging", paging);
		req.setAttribute("pageNo", current_page);
		req.setAttribute("replyList", replyList);
		req.setAttribute("replyCount", replyCount);
		req.setAttribute("total_page", total_page);
		
		// listReply.jsp�� ������
		forward(req, resp, "/WEB-INF/views/board/listReply.jsp");
		
		
	}
	
	private void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ��� ����
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		BoardDAO dao = new BoardDAO();
		
		// ���� ��ư�� data-replyNum���� �޾ƿ� replyNum
		int replyNum = Integer.parseInt(req.getParameter("replyNum"));
		String state = "false";
		// ������ �� ���� ���� �޾Ƽ�
		int result = dao.deleteReply(replyNum, info.getUserId());
		// 1�� ���� �����Ǿ�����(����ó���̸�)
		if(result == 1)
			// state�� true�� ����
			state = "true";
		
		// JSON ���·� state ����
		JSONObject job=new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());	
	}


	private void insertBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ���ƿ� �߰�
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		BoardDAO dao = new BoardDAO();
		
		String state = "false";
		// ��ư�� ������ �ִ� num���� �޾ƿ�
		int num = Integer.parseInt(req.getParameter("num"));
		// �߰��� ���� ���� ���� ����
		int result = dao.insertBoardLike(num, info.getUserId());
		// 1�� ���� �߰� �Ǿ��ٸ� true
		if(result == 1) {
			state = "true";
		// 0�� ���� �߰� �Ǿ��ٸ� false
		} else if(result == 0) {
			state = "false";
		}
		// JSON ���·� state ������ ����
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}
	
	private void countBoardLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ���ƿ� ���� ����
		BoardDAO dao = new BoardDAO();
		// countBoardLike��� �̸��� function�� param���� �޾ƿ� num
		int num = Integer.parseInt(req.getParameter("num"));
		// ���ƿ� ���� �޾Ƽ�
		int countBoardLike = dao.countBoardLike(num);
		
		// JSON ���·� ������ ����(list.jsp�� function)
		JSONObject job = new JSONObject();
		job.put("countBoardLike", countBoardLike);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
	}

	private void profileMain(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ������ ȭ��
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		// �ּҰ� or ����Ʈ�� ���̵��� ���� id �޾ƿ�
		String id = req.getParameter("id");
		String fanId = info.getUserId();
		// id�� �ش��ϴ� ������ �д´�.
		MemberDTO dto = dao.readMember(id);
		// ���� dto�� ����ȭ������ �̵�
		if(dto == null) {
			resp.sendRedirect(cp+"/board/main.do");
			return;
		}
		// follow ��ư ó���� ���� üũ
		String following = "false";
		int result = dao.readFollow(fanId, id);
		if(result == 1) {
			following = "true";
		} else if(result == 0) {
			following = "false";
		}
		
		// �װ� �ƴϸ� profile.jsp�� ������
		req.setAttribute("dto", dto);
		req.setAttribute("following",following);
		forward(req,resp,"/WEB-INF/views/board/profile.jsp");
	}
	
	private void profileList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���� ������ �Խù� ���
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		// profile.jsp���� id, page�� �޾ƿ�
		String page = req.getParameter("pageNo");
		String id = req.getParameter("id");

		int current = 1;
		if(page != null)
			current = Integer.parseInt(page);
		
		int rows = 8;
		// ������ ī��Ʈ ���� �������� ��ƾ���
		int dataCount = dao.dataCount(id);
		int total_page = util.pageCount(rows, dataCount);
		
		if(current>total_page)
			current=total_page;
		
		int start = (current-1)*rows+1;
		int end = current*rows;
		
		// DAO �ҷ�����
		List<BoardDTO> list = dao.listBoard(start, end, id);
		for(BoardDTO dto : list) {
			dto.setContent(util.htmlSymbols(dto.getContent()));
		}
		
		String articleUrl = cp+"/board/article.do?page="+current;
		
		String paging = util.paging(current, total_page);
		
		// �������� ������
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("list", list);
		req.setAttribute("total_page", total_page);
		req.setAttribute("current", current);
		req.setAttribute("articleUrl", articleUrl);
		
		// pfList.jsp�� ������
		forward(req,resp,"/WEB-INF/views/board/pfList.jsp");
	}
	
	private void main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		// ��ü ����Ʈ ȭ������ �̵�
		forward(req,resp,"/WEB-INF/views/board/list.jsp");
		
	}

	// ��ü ����Ʈ
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		
		// function listPage(page) ���� �޾ƿ� ��
		String page = req.getParameter("pageNo");
		
		int current = 1;
		if(page != null)
			current = Integer.parseInt(page);
		
		// �� �������� ������ �Խù� ��
		int rows = 3;
		// ��ü �Խù� ��
		int dataCount = dao.dataCount();
		// ��ü ������ ��
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
		
		// JSON �������� ������ ������ => list.jsp�� ����Ʈ ���� �κ����� ������
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
		
		// JSON �������� ������ ������ => list.jsp�� ����Ʈ ���� �κ����� ������
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
		// �Խù� �ۼ�ȭ������ �̵�
		
		// �̵��ؿ� ȭ���� list���� profile���� �����ϱ� ���� screen��. ������� �Ѿ�´�.
		String screen = req.getParameter("screen");
		
		req.setAttribute("screen", screen);
		// �߰� or ������ �����ϱ� ���� mode => created�� �����Ѵ�.
		req.setAttribute("mode", "created");
		// created.jsp�� ������ => �Խù� �߰� ȭ�� ����
		forward(req, resp, "/WEB-INF/views/board/created.jsp");
	}
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� �߰�
		String cp = req.getContextPath();
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = new BoardDTO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		// ���� ���ε带 ���� ������ ����
		String encType = "utf-8";
		// �ִ� ���� ũ��
		int maxSize = 5*1024*1024;
		
		// ���� ���ε带 ���� MultipartRequest ���
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		String screen = mreq.getParameter("screen");
		
		if(mreq.getFile("upload") != null) {
			dto.setId(info.getUserId());
			dto.setContent(mreq.getParameter("content"));
			dto.setPlace(mreq.getParameter("p_input"));
			
			// ���ε� â�� �ö�� ���ϸ�
			String saveName = mreq.getFilesystemName("upload");
			// ���ε�� �ð����� �̸� �������ִ� method
			saveName = FileManager.doFilerename(pathname, saveName);
			
			dto.setImgName(saveName);
			// ������ �߰�
			dao.insertBoard(dto);
			// �̵��ؿ� �������� ���� �̵��� ������ ����
			if(screen.equals("list")) {
				resp.sendRedirect(cp+"/board/main.do");				
			} else if(screen.equals("profile")) {
				resp.sendRedirect(cp+"/board/profile.do?id="+info.getUserId());	
			}
		}
		
	}
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// profile���� �Խù� ��ȸ(1����)
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		// �̹����� data-num �Ӽ��� ������ �ִ� �Խù� ��ȣ �޾ƿ�
		int num = Integer.parseInt(req.getParameter("num"));
		
		// �޾ƿ� �Խù� ��ȣ�� ��ȸ
		BoardDTO dto = dao.readBoard(num);
		dto.setContent(util.htmlSymbols(dto.getContent()));
		
		// dto�� �Բ� ������
		req.setAttribute("dto", dto);
		forward(req, resp, "/WEB-INF/views/board/article.jsp");
	}
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ���� ȭ������ �̵�
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		// ��ư�� �������ִ� �Ӽ������� �޾ƿ� num
		int num = Integer.parseInt(req.getParameter("num"));
		// ���� ������ ����
		String screen = req.getParameter("screen");
		
		// �޾ƿ� ��ȣ�� ������ ����
		BoardDTO dto = dao.readBoard(num);
		if(dto == null) {
			resp.sendRedirect(cp+"/board/main.do");
			return;
		}
		
		// �� �ۼ��ڿ� ���� ����ڰ� �ٸ��ٸ�
		if(! info.getUserId().equals(dto.getId())) {
			resp.sendRedirect(cp+"/board/main.do");
			return;
		}
		
		// mode�� �Խù� ���� + screen�� dto�� ������ created.jsp�� ������
		req.setAttribute("mode", "update");
		req.setAttribute("screen", screen);
		req.setAttribute("dto", dto);
		forward(req, resp, "/WEB-INF/views/board/created.jsp");
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		String encType = "utf-8";
		// ���� �ִ� ũ��
		int maxSize = 5*1024*1024;
		
		// ���� ���ε带 ���� MultipartRequest ���
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		
		BoardDTO dto = new BoardDTO();
		
		String screen = mreq.getParameter("screen");
		dto.setNum(Integer.parseInt(mreq.getParameter("num")));
		dto.setContent(mreq.getParameter("content"));
		dto.setPlace(mreq.getParameter("p_input"));
		
		String imgName = mreq.getParameter("imgName");
		if(mreq.getFile("upload") != null) {
			FileManager.doFiledelete(pathname, imgName);
			
			// ���� ���� â���� �޾ƿ� �̸�
			String saveFilename = mreq.getFilesystemName("upload");
			// ���� �̸� �缳��
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setImgName(saveFilename);
		} else {
			// ���� ���� ���� ���
			dto.setImgName(imgName);
		}
		// �Խù� ����
		dao.updateBoard(dto);
		
		// screen ���� ���� �̵� �� ������ ������
		if(screen.equals("list")) {
			resp.sendRedirect(cp+"/board/main.do");			
		} else if(screen.equals("profile")) {
			resp.sendRedirect(cp+"/board/profile.do?id="+info.getUserId());		
		}
		
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����
		BoardDAO dao = new BoardDAO();
		// ��ư���� �޾ƿ� num
		int num = Integer.parseInt(req.getParameter("num"));
		// ���� �����ϸ�
		String state = "false";
		String screen = req.getParameter("screen");
		dao.deleteBoard(num);
		// state�� true�� ����
		state = "true";
		
		// JSON ���·� ������ ����
		JSONObject job = new JSONObject();
		job.put("state", state);
		job.put("screen", screen);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}

}
