import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import javax.swing.*
import de.ipcon.swing.ViewerPanel
import org.apache.log4j.*
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.print.PrinterJob;
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.rendering.PageDrawer
import org.apache.pdfbox.rendering.PDFPrinter
import org.apache.pdfbox.rendering.printing.Scaling
import org.apache.pdfbox.rendering.printing.Orientation
import javax.imageio.ImageIO

PropertyConfigurator.configureAndWatch('log4j.conf')

def pdf=PDDocument.load(new File(args[0]))
def pages = pdf.getDocumentCatalog().getAllPages()
println pdf.getNumberOfPages()
println pages.size()
println pages[0]
println pages[0].getCropBox()

long start=System.currentTimeMillis()
def printperf={ msg ->
   println "$msg (${System.currentTimeMillis()-start}ms)"
   start=System.currentTimeMillis()
}
/*
[400,800,1200].each{ res ->
   println "\nrender to $res*$res\n"
   def renderer=new PDFRenderer(pdf)
   def drawer=new PageDrawer(renderer)
   def page=pages[0]
   def mb=page.getMediaBox()
   def scale=res/mb.width
   println "media width=$mb.width -> scale=$scale"
   def img=new BufferedImage(res,(mb.height*scale) as int,BufferedImage.TYPE_INT_RGB)
   def gfx=img.createGraphics()
   println gfx
   setRenderingHintMax(gfx)
   gfx.scale(scale,scale)
   drawer.drawPage(gfx,page,mb)
   ImageIO.write(img,'jpeg',new File("out${res}.jpg"))
   printperf "rendering"
}
*/


void setRenderingHintMax(Graphics2D g) {
   g.setRenderingHint(RenderingHints.KEY_RENDERING,            RenderingHints.VALUE_RENDER_QUALITY)
   g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,      RenderingHints.VALUE_COLOR_RENDER_QUALITY)
   g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,  RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
   g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,        RenderingHints.VALUE_INTERPOLATION_BILINEAR)//  : RenderingHints.VALUE_INTERPOLATION_BICUBIC);BILINEAR
   g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,         RenderingHints.VALUE_ANTIALIAS_ON)
   g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB) // fixme: platform-hint
   g.setRenderingHint(RenderingHints.KEY_DITHERING,            RenderingHints.VALUE_DITHER_ENABLE)
   g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,    RenderingHints.VALUE_FRACTIONALMETRICS_ON)
}


SwingUtilities.invokeAndWait{

   def f=new JFrame( title:"AnnotatedViewerPanel Test", defaultCloseOperation:JFrame.EXIT_ON_CLOSE )
   f.setSize(800,900)
   def cb=new ViewerPanel()
   cb.setDocument(pdf)
   cb.showPage(1,true)
   def sp=new JScrollPane(cb)
   f.add(sp)

   //f.add(cb)
   //f.pack()
   f.show()

}

/*
def pri=new PDFPrinter(pdf,Scaling.SHRINK_TO_FIT,Orientation.AUTO)
pri.print(PrinterJob.getPrinterJob())


SwingUtilities.invokeAndWait{
      println "rendering took ${System.currentTimeMillis()-start}ms"
}

sleep 100000
*/
