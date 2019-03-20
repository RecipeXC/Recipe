package business;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import com.util.MybatisUtils;

import entity.User;

public class UserBusi {
	
	public int login(User user) throws SQLException {
		String pass = "";
		SqlSession sqlSession = MybatisUtils.getSession();
		try {
			pass = sqlSession.selectOne("mapper.UserMapper.selectPassword",user);
		}finally{
			sqlSession.close();
		}
		
		if(pass.equals(user.getPassword())) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public int register(User user) throws SQLException {
		int r = 0;
		SqlSession sqlSession = MybatisUtils.getSession();
		try {
			r = sqlSession.insert("mapper.UserMapper.insertUser",user);
		    if(r == 1){
		        System.out.println("���ɹ������� "+r+" ���û���");
		        sqlSession.commit();
		    }else{
		        System.out.println("ִ�в����û�����ʧ�ܣ�����");
		    }
		}finally{
			sqlSession.close();
		}
		
		return r;
	}

	public User getAllInfo(User user) {
		
		SqlSession sqlSession = MybatisUtils.getSession();
		try {
			user = sqlSession.selectOne("mapper.UserMapper.selectUser",user);
		}finally{
			sqlSession.close();
		}
		
		return user;
	}
	
}
