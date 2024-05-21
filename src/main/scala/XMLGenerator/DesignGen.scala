package XMLGenerator

import XMLGenerator.SharedXMLGenerator.designToXml
import java.io.{File, PrintWriter}
import scala.xml.{Elem, Utility}
import java.nio.file.{Files, Paths}
object DesignGen extends App{
  // 示例用法
  val componentRef1 = ComponentRef("HWComponent1", "flat", "HWComponent888", "1.0")

  val componentInstance1 = ComponentInstance("HW_Component888_0", componentRef1)

  val portReference1 = PortReference("HW_Component888_0", "port")
  val portReference2 = PortReference("", "port")
  val portReference4 = PortReference("", "port")
  val portReference6 = PortReference("", "port_0")

  val adHocConnection1 = AdHocConnection("HW_Component888_0_port_to_port", Seq(portReference1, portReference2))

  val design = Design("HWDesign", "flat", "Dsign1.design", "1.0",
    Seq(componentInstance1),
    Seq(adHocConnection1))

  val xml = designToXml(design)

  val xmlString = """<?xml version="1.0" encoding="UTF-8"?>""" + "\n" + Utility.serialize(xml)

  // 将格式化后的 XML 写入文件
  val basePath = Paths.get(".", "Kactus2", "HWDesign", "flat", "Dsign1", "1.0")
  Files.createDirectories(basePath) // 确保目录存在
  val filePath = basePath.resolve("Dsign1.design.1.0.xml").toString
  val writer = new PrintWriter(new File(filePath))
  writer.write(xmlString.toString)
  writer.close()

}
