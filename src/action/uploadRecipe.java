package action;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import business.IncludeBusi;
import business.RecipeBusi;
import business.StepBusi;
import entity.Include;
import entity.Recipe;
import entity.Step;

/**
 * Servlet implementation class uploadRecipe
 */
@WebServlet("/uploadRecipe")
public class uploadRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public uploadRecipe() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());

		int result = 0;

		//�ϴ�ʳ�׵�һ����
		Recipe recipe = new Recipe();
		RecipeBusi recbus = new RecipeBusi();
		
		recipe.setAuthor("17379506118");
		System.out.println(request.getParameter("recipe_name").trim());
		recipe.setName(request.getParameter("recipe_name").trim());
		recipe.setCategory(request.getParameter("category").trim());
		recipe.setComplexity(request.getParameter("summary").trim());
		recipe.setMinute(Integer.parseInt(request.getParameter("minute").trim()));
		recipe.setTasty(request.getParameter("tasty").trim());
		recipe.setMethod(request.getParameter("method").trim());
		recipe.setDescription(request.getParameter("description").trim());
		recipe.setAddress(request.getParameter("directions").trim());
		
		result = recbus.upload(recipe);
		if(result == 1) {
			System.out.println("ʳ�׵�һ�����ϴ��ɹ���");
		}else {
			String html = "ʳ�׵�һ�����ϴ�ʧ�ܣ�<br><a href='submit-recipe.jsp'>�����ϴ�</a>";
			response.getWriter().write(html);
		}
		
		recipe.setId(recbus.getMaxId());
		
		//�ϴ�����
		String[] steps = null;
		Step step = new Step();
		StepBusi stebus = new StepBusi();
		
		steps = request.getParameterValues("step");
		for(int i=0;i<steps.length;i++) {
			result = 0;
			step.setRecipe(recipe.getId());
			step.setSequence(i+1);
			step.setDescription(steps[i]);
			result = stebus.upload(step);
			if(result == 1) {
				System.out.println("����  "+i+"  �ϴ��ɹ���");
			}else {
				String html = "�����ϴ�ʧ�ܣ�<br><a href='submit-recipe.jsp'>�����ϴ�</a>";
				response.getWriter().write(html);
			}
		}
		
		//�ϴ�ʳ��
		String[] includes = request.getParameterValues("ingredient_name");
		String[] Mquantities = request.getParameterValues("ingredient_note");
		Include include = new Include();
		IncludeBusi incbus = new IncludeBusi();
		
		for(int i=0;i<includes.length;i++) {
			result = 0;
			include.setRecipe(recipe.getId());
			include.setMaterial(Integer.parseInt(includes[i]));
			include.setQuantity(Mquantities[i]);
			result = incbus.upload(include);
			if(result == 1) {
				System.out.println("ʳ��  "+include.getMaterial()+"  �ϴ��ɹ���");
			}else {
				String html = "ʳ���ϴ�ʧ�ܣ�<br><a href='submit-recipe.jsp'>�����ϴ�</a>";
				response.getWriter().write(html);
			}
		}
		
		//�ϴ�ͼƬ
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart) {//�ж�ǰ̨��form�Ƿ���multipart����
//			FileItemFactory factory = new DiskFileItemFactory();
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//ͨ��parseRequest����form�е����������ֶΣ������浽items������
			try {
				//�����ϴ��ļ�ʱ�õ�����ʱ�ļ��Ĵ�СDiskFileItemFactory
				factory.setSizeThreshold(10485760);//������ʱ�ļ���������СΪ10MB(��λΪ�ֽ�B)
				factory.setRepository(new File("D:\\Course\\Java\\workplace\\Recipe\\uploadtemp"));//������ʱ�ļ���Ŀ¼
				
				//�����ϴ������ļ��Ĵ�С  �˴�Ϊ100MB
				upload.setSizeMax(104857600);
				
				List<FileItem> items = upload.parseRequest(request);
				//����items�е�����(item=sno, sname, file)
				Iterator<FileItem> iter = items.iterator();
				while(iter.hasNext()) {
					FileItem item = iter.next();
					String itemName = item.getFieldName();
					//�ж�ǰ̨�ֶΣ�����ͨform���ֶΣ������ļ��ֶ�
					
					//request.getParameter()   --iter.getString
					if(item.isFormField()) {//��ͨform���ֶ��ϴ�
						System.out.println("������ͨform���ֶ��ϴ�");
					}else {
						if(itemName.equals("picture")) {
							//file �ļ��ϴ�
							//getFieldName�ǻ�ȡ��ͨ���ֶ�nameֵ
							//getName�ǻ�ȡ�ļ���
							String fileName = item.getName();
							if(fileName.contains("\\")) {
		                        //����������ȡ�ַ���
								fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
		                    }
							System.out.println("�ļ�����"+fileName);
							//��ȡ�ļ����ݲ��ϴ�
							//�����ļ�·����ָ���ϴ���λ�ã�������·����������ŵ�workplace�±����̵�һ���ļ���
							String path = "D:\\Course\\Java\\workplace\\Recipe\\WebContent\\upload\\recipe\\picture";
							System.out.println("�ļ�����·����"+path);
							File file = new File(path,fileName);
							item.write(file);//�ϴ�
							System.out.println(fileName+"�ϴ��ɹ���");
						}
					}
				}
			} catch(FileUploadBase.SizeLimitExceededException e) {
				System.out.println("�ϴ��ļ���С�������ƣ����100MB");
				String html = "ͼƬ�ϴ�ʧ�ܣ�<br>�ϴ��ļ���С�������ƣ����100MB!<br><a href='submit-recipe.jsp'>�����ϴ�</a>";
				response.getWriter().write(html);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("ǰ̨��form��multipart���ԣ�");
			System.out.println("ͼƬ�ϴ�ʧ�ܣ�");
			String html = "ͼƬ�ϴ�ʧ�ܣ�<br>ǰ̨��form��multipart���ԣ�<br><a href='submit-recipe.jsp'>�����ϴ�</a>";
			response.getWriter().write(html);
		}
		
		String html = "����ʳ���ϴ��ɹ��������ĵȴ����...<br><a href='submit-recipe.jsp'>�����ϴ�ҳ��</a>";
		response.getWriter().write(html);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
