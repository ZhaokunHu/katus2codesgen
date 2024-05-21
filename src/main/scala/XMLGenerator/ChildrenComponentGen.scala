package XMLGenerator

import XMLGenerator.SharedXMLGenerator.childrenComponentToXml
import java.nio.file.{Files, Paths}
import java.io.{File, PrintWriter}
import scala.xml.{Elem, Utility}

object ChildrenComponentGen extends App {

  // 创建实例
  val vector = Vector(left = 7, right = 0)
  val vectors = Vectors(vector = vector)
  val wireIn = Wire(direction = "in", vectors = vectors)
  val wireOut = Wire(direction = "out", vectors = vectors)
  val portIn = Port(name = "port", wire = wireIn, generateVendorExtensions = true)
  val portOut = Port(name = "port_0", wire = wireOut)
  val ports = Ports(port = Seq(portIn, portOut))

  val vendorExtensions = VendorExtensions(kactus2_version = "3,13,2,0")

  val model = ChildrenModel(ports = ports, vendorExtensions = vendorExtensions)

  val ktsAttributes = KtsAttributes(kts_productHier = "Flat", kts_implementation = "HW", kts_firmness = "Mutable")
  val kactus2Attributes = Kactus2Attributes(kts_attributes = ktsAttributes)
  val attributes = Attributes(kactus2_attributes = kactus2Attributes)

  val component = ChildrenComponent(
    vendor = "HWComponent1",
    library = "flat",
    name = "HWComponent888",
    version = BigDecimal(1.0),
    model = model,
    attributes = attributes
  )

  // 自定义生成 XML 的函数

  // 生成 XML
  val xml = childrenComponentToXml(component)

  val xmlString = """<?xml version="1.0" encoding="UTF-8"?>"""+"\n"+Utility.serialize(xml)

  // 将格式化后的 XML 写入文件
  val basePath = Paths.get(".", "Kactus2", "HWComponent1", "flat", "children1", "1.0")
  Files.createDirectories(basePath) // 确保目录存在
  val filePath = basePath.resolve("children1.1.0.xml").toString
  val writer = new PrintWriter(new File(filePath))
  writer.write(xmlString.toString)
  writer.close()
}
