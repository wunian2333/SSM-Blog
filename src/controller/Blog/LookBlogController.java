package controller.Blog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pojo.TbBlogartical;
import pojo.TbBloguser;
import pojo.TbComment;
import service.GetFansNum;
import service.GetFollowNum;
import service.LookBlog;
import service.UserInfo;
/**
 * 
* <p>Title: LookBlogController.java<／p>
* <p>Description: 查看相应的博客的controller<／p>
* @author Ma
* @date 2019年5月23日
 */
@Controller
public class LookBlogController {

	public static volatile Map<Integer, Integer> ReadBLogNumber = new ConcurrentHashMap<>();

	@Autowired
	private LookBlog service;	
	@Autowired
	private UserInfo userinfo;
	@Autowired
	private GetFansNum FanService;
	@Autowired
	private GetFollowNum FollowService;
	@RequestMapping("/LookBlog")
	public String lookblog(
			@RequestParam(value = "FansNum", defaultValue = "-1")Integer FansNum,
			@RequestParam(value = "FollowNum", defaultValue = "-1")Integer FollowNum,Integer BlogId, String UserNumber,String UserName, ModelMap model) throws Exception {
		
		if("".equals(UserNumber)) {
				return "redirect:/Login";
		}			
		else if (!ReadBLogNumber.containsKey(BlogId)) {  //不包含BlogID  //采用单例模式来实现只会往ReadBlogNumber中添加一次key=Blog的情况
			synchronized(LookBlogController.class) {
				if(!ReadBLogNumber.containsKey(BlogId)) {
					TbBlogartical blog = service.GetBlog(BlogId);
					
					
					
					
					ReadBLogNumber.put(BlogId,blog.getBlogtraffic()+1);
					//当前用户的昵称
					model.put("UserName", UserName);
					
					TbBloguser user = userinfo.GetUserInfo(blog.getBlogusernumber()) ;
					//博客主人的昵称
					model.put("BlogUserName",user.getBlogusername());
					//博客主人的number
					model.put("BlogUserNumber", blog.getBlogusernumber());
					
					if(FansNum==-1) {  //当前是没有查询博客用户的粉丝数和关注数						
						FansNum = FanService.getfansnum(blog.getBlogusernumber());
						FollowNum = FollowService.getfollow(blog.getBlogusernumber());						
					}	
					boolean isflow = FollowService.isfollow(UserNumber, blog.getBlogusernumber());
					model.put("isfollow",isflow==true?1:0);
					//当前用户的number
					model.put("UserNumber",UserNumber);
					model.put("Title", blog.getBlogtitle());
					model.put("Content", blog.getBlogcontent());
					model.put("BlogDate",blog.getBlogdata());
					model.put("BlogTraffic",ReadBLogNumber.get(BlogId));
					model.put("BlogId",BlogId);
					model.put("FollowNum",FollowNum);
					model.put("FansNum",FansNum);
					
					/*获取评论列表*/
					List<TbComment> list = service.GetBlogComment(BlogId);
//					for(TbComment x:list) {
//						System.out.println(x.getCommentContent());
//					}
					model.put("sum",list.size());
					model.put("Date",list);
					return "Blog/LookBlog";
				} else {
					ReadBLogNumber.put(BlogId,ReadBLogNumber.get(BlogId)+1);
					TbBlogartical blog = service.GetBlog(BlogId);
					//当前用户的昵称
					model.put("UserName", UserName);
					TbBloguser user = userinfo.GetUserInfo(blog.getBlogusernumber()) ;
					//博客主人的昵称
					model.put("BlogUserName",user.getBlogusername());
					model.put("BlogDate",blog.getBlogdata());
					//博客主人的number
					model.put("BlogUserNumber", blog.getBlogusernumber());
					
					if(FansNum==-1) {  //当前是没有查询博客用户的粉丝数和关注数
						
						FansNum = FanService.getfansnum(blog.getBlogusernumber());
						FollowNum = FollowService.getfollow(blog.getBlogusernumber());						
					}
					boolean isflow = FollowService.isfollow(UserNumber, blog.getBlogusernumber());
					model.put("isfollow",isflow==true?1:0);
					model.put("FollowNum",FollowNum);
					model.put("FansNum",FansNum);
					//当前用户的number
					model.put("UserNumber",UserNumber);
					model.put("Title", blog.getBlogtitle());
					model.put("Content", blog.getBlogcontent());
					model.put("BlogTraffic",ReadBLogNumber.get(BlogId));
					model.put("BlogId",BlogId);
					/*获取评论列表*/
					List<TbComment> list = service.GetBlogComment(BlogId);
					model.put("sum",list.size());
					model.put("Date",list);
//					for(TbComment x:list) {
//						System.out.println(x.getCommentContent());
//					}
					return "Blog/LookBlog";
				}
			}			
		} else {          //包含BlogID			
			ReadBLogNumber.put(BlogId,ReadBLogNumber.get(BlogId)+1);
			TbBlogartical blog = service.GetBlog(BlogId);
			//当前用户的昵称
			model.put("UserName", UserName);
			TbBloguser user = userinfo.GetUserInfo(blog.getBlogusernumber()) ;
			//博客主人的昵称
			model.put("BlogUserName",user.getBlogusername());
			//博客主人的number
			model.put("BlogUserNumber", blog.getBlogusernumber());
			if(FansNum==-1) {  //当前是没有查询博客用户的粉丝数和关注数
				
				FansNum = FanService.getfansnum(blog.getBlogusernumber());
				FollowNum = FollowService.getfollow(blog.getBlogusernumber());
				
			}
			boolean isflow = FollowService.isfollow(UserNumber, blog.getBlogusernumber());
			model.put("isfollow",isflow==true?1:0);
			model.put("FollowNum",FollowNum);
			model.put("FansNum",FansNum);
			//当前用户的number
			model.put("UserNumber",UserNumber);
			model.put("BlogDate",blog.getBlogdata());
			model.put("Title", blog.getBlogtitle());
			model.put("Content", blog.getBlogcontent());
			model.put("BlogTraffic",ReadBLogNumber.get(BlogId));
			model.put("BlogId",BlogId);
			/*获取评论列表*/
			List<TbComment> list = service.GetBlogComment(BlogId);
//			for(TbComment x:list) {
//				System.out.println(x.getCommentContent());
//			}
			model.put("sum",list.size());
			model.put("Date",list);
			return "Blog/LookBlog";
		}
	}
}
