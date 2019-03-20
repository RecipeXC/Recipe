package business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.util.MybatisUtils;

import dao.DAO;
import entity.Picture;
import entity.Recipe;

public class PictureBusi {
	
	private Connection con = null;
	private PreparedStatement ppst = null;
	private ResultSet rs = null;
	private int rs_2;
	private String sql = "";
	Object a[] = null;
	
	public List<Picture> getPicture(Recipe recipe){
		List<Picture> list = new ArrayList<Picture>();
/*		a = new Object[1];
		sql="select number,url from r_picture where recipe = ? ;";
		a[0] = recipe.getId();
		try {
			con = DAO.getConnection();
			ppst = DAO.getPreparedStatement(con, sql, a);
			rs = DAO.getResultSet(ppst);
			while(rs.next()){	
				Picture picture = new Picture();
				picture.setNumber(rs.getInt(1));
				picture.setUrl(rs.getString(2));
				list.add(picture);
			}	
			DAO.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		SqlSession sqlSession = MybatisUtils.getSession();
		try {
			list = sqlSession.selectList("mapper.PictureMapper.selectPictureByRecipeId",recipe);
		}finally {
			sqlSession.close();
		}
		
		return list;
	} 
	
	public int upload(Picture picture) {
		int r = 0;
/*		sql = "insert into r_picture (recipe,number,url) value(?,?,?) ;";
		a = new Object[3];
		a[0] = picture.getRecipe();
		a[1] = picture.getNumber();
		a[2] = picture.getUrl();
		try {
			con = DAO.getConnection();
			ppst = DAO.getPreparedStatement(con, sql, a);	
			rs_2 = DAO.update(ppst);
			DAO.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		r = rs_2;*/
		
		SqlSession sqlSession = MybatisUtils.getSession();
		try {
			r = sqlSession.insert("mapper.PictureMapper.insertPicture",picture);
			if(r == 1){
		        System.out.println("���ɹ������� "+r+" ��ͼƬ��");
		        sqlSession.commit();
		    }else{
		        System.out.println("ִ�в���ͼƬ����ʧ�ܣ�����");
		    }
		}finally {
			sqlSession.close();
		}
		
		return r;
	}

}
