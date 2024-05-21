package XMLGenerator

import XMLGenerator.SharedXMLGenerator.designConfigurationToXml
import java.io.{File, PrintWriter}
import scala.xml.{Elem, Utility}
import java.nio.file.{Files, Paths}
object DesignCfgGen extends App {
  val designRef = DesignRef("HWDesign", "flat", "Dsign1.design", "1.0")
  val ktsAttributes = Kactus2AttributesImplementation("HW")
  val vendorExtensions = VendorExtensionsWithAttributes("3,13,2,0", ktsAttributes)
  val designConfig = DesignConfiguration("HWDesign", "flat", "Dsign1.designcfg", "1.0", designRef, vendorExtensions)

  val xml = designConfigurationToXml(designConfig)
  val xmlString = """<?xml version="1.0" encoding="UTF-8"?>"""+"\n"+Utility.serialize(xml)

  // 将格式化后的 XML 写入文件
  val basePath = Paths.get(".", "Kactus2", "HWDesign", "flat", "Dsign1", "1.0")
  Files.createDirectories(basePath) // 确保目录存在
  val filePath = basePath.resolve("Dsign1.designcfg.1.0.xml").toString
  val writer = new PrintWriter(new File(filePath))
  writer.write(xmlString.toString)
  writer.close()
}
