package twb.utils.img;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

/**
 * 
 * @Title:  ImageCompress.java   
 * @Package twb.utils.img   
 * @Description:    压缩图片 
 * @author: 田文彬     
 * @date:   2019年10月17日 下午3:35:59   
 * @version V1.0
 */
public class ImageCompress {
	private  float imageQuality = 1;

	public byte[] compressImage(byte[] imageDate,long fileSize, int imgMaxSize, int imgMinSize ){
		Map paraMap = new HashMap();

		int count = 0;

		while(fileSize >  imgMaxSize*1024&&count<5){		
			Image image = null;
			BufferedImage imageBuff = null;
			//压缩比按照片最大值设定，尽量避免图像失真
			imageQuality = (float)imgMaxSize*1024/fileSize;
//			imageQuality = (float)(imgMaxSize+imgMinSize)*1024/2/fileSize;

			try {
			    ByteArrayInputStream bais = new ByteArrayInputStream(imageDate);
			    ImageIO.setUseCache(false);
			    image = ImageIO.read(bais);
			    
			    /*先压缩尺寸到合适显示大小*/
				// 为等比缩放计算输出的图片宽度及高度,以高度缩放比例为准
				double rate = 1;
//				if(image.getHeight(null)*imageQuality < 800){
//					rate = (double)800/image.getHeight(null);
//				}
				// 根据缩放比率大的进行缩放控制
				int newWidth = (int) (((double) image.getWidth(null)) * rate);
				int newHeight = (int) (((double) image.getHeight(null)) * rate);
			    
			    imageBuff = new BufferedImage((int) newWidth,(int) newHeight,BufferedImage.TYPE_INT_RGB);   
			    /*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
			    imageBuff.getGraphics().drawImage(
			    		image.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
			    bais.close();
			} catch (Exception e) {
//			    image = (new JPanel().getToolkit().createImage(imageDate));
//				// 为等比缩放计算输出的图片宽度及高度
//				double rate = imageQuality;
//				if(image.getHeight(null)*imageQuality < 800){
//					//rate = (double)800/image.getHeight(null);
//				}
//				// 根据缩放比率大的进行缩放控制
//				int newWidth = (int) (((double) image.getWidth(null)) * rate);
//				int newHeight = (int) (((double) image.getHeight(null)) * rate);
//			    imageBuff = new BufferedImage((int) newWidth,(int) newHeight,BufferedImage.TYPE_INT_RGB);   
//			    /*
//				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
//				 */
//			    imageBuff.getGraphics().drawImage(
//			    		image.getScaledInstance(newWidth, newHeight,
//								Image.SCALE_SMOOTH), 0, 0, null);
 
			}
			imageDate = bufferedImageTobytes(imageBuff,1);
			fileSize = imageDate.length;
			/*如照片尺寸压缩后仍大于照片大小限制，重算压缩比， 进行质量压缩*/
			if(fileSize >  imgMaxSize*1024){
			  /*压缩比按照片最大值设定，尽量避免图像失真*/
			  imageQuality = (float)imgMaxSize*1024/fileSize;
			  //imageQuality = (float)0.85;

			  imageDate = bufferedImageTobytes(imageBuff,imageQuality);
			  fileSize = imageDate.length;
			}
			count++;
		}

		if(fileSize >  imgMaxSize*1024){
			paraMap.put("result", false);
		}else{
			paraMap.put("result", true);
		}
		
		return imageDate ;
	}
	
	
	
    /** 
     *  
     * 设置压缩质量来把图片压缩成byte[] 
     *  
     * @param image 
     *            压缩源图片 
     * @param quality 
     *            压缩质量，在0-1之间， 
     * @return 返回的字节数组 
     */  
    private byte[] bufferedImageTobytes(BufferedImage image, float quality) {  
        // 如果图片空，返回空  
        if (image == null) {  
            return null;  
        }  
        // 得到指定Format图片的writer  
        Iterator<ImageWriter> iter = ImageIO  
                .getImageWritersByFormatName("jpeg");// 得到迭代器  
        ImageWriter writer = (ImageWriter) iter.next(); // 得到writer  
  
        // 得到指定writer的输出参数设置(ImageWriteParam )  
        ImageWriteParam iwp = writer.getDefaultWriteParam();  
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩  
        iwp.setCompressionQuality(quality); // 设置压缩质量参数  
  
        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);  
  
        ColorModel colorModel = ColorModel.getRGBdefault();  
        // 指定压缩时使用的色彩模式  
        iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,  
                colorModel.createCompatibleSampleModel(16, 16)));  
  
        // 开始打包图片，写入byte[]  
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流  
        IIOImage iIamge = new IIOImage(image, null, null);  
        try {  
            // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput  
            // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput  
            writer.setOutput(ImageIO  
                    .createImageOutputStream(byteArrayOutputStream));  
            writer.write(null, iIamge, iwp); 
        } catch (IOException e) {  
//            System.out.println("write errro");//屏蔽sysout By wang.ganzhong 2017-11-18 16:29:08  
            e.printStackTrace(); 
            try {
				this.finalize();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }  

        return byteArrayOutputStream.toByteArray();  
    } 
}