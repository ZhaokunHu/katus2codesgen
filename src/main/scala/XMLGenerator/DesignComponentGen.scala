package XMLGenerator

import XMLGenerator.SharedXMLGenerator.designComponentToXml

import java.io.{File, PrintWriter}
import scala.xml.{Elem, Utility}
import java.nio.file.{Files, Paths}
object DesignComponentGen extends App {

  // 创建实例
  val vector = Vector(left = 6, right = 0)
  val vectors = Vectors(vector = vector)
  val wireIn = Wire(direction = "in", vectors = vectors)
  val wireOut = Wire(direction = "out", vectors = vectors)
  val portIn = Port(name = "port", wire = wireIn,generateVendorExtensions = true)
  val portOut = Port(name = "port_0", wire = wireOut)
  val ports = Ports(port = Seq(portIn, portOut))

  val designConfigRefAttributes = Map(
    "@vendor" -> "HWDesign",
    "@library" -> "flat",
    "@name" -> "Dsign1.designcfg",
    "@version" -> "1.0"
  )
  val designConfigurationRef = DesignConfigurationRef(attributes = designConfigRefAttributes)

  val designConfigurationInstantiation = DesignConfigurationInstantiation(
    name = "Design1.designcfg_1.0",
    designConfigurationRef = designConfigurationRef
  )
  val instantiations = Instantiations(designConfigurationInstantiation = designConfigurationInstantiation)

  val view = View(name = "hierarchical", designConfigurationInstantiationRef = "Design1.designcfg_1.0")
  val views = Views(view = view)
  val vendorExtensions = VendorExtensions(kactus2_version = "3,13,2,0")

  val model = DesignModel(views = views, instantiations = instantiations, ports = ports,vendorExtensions = vendorExtensions)

//  val componentAttributes = Map(
//    "@{http://www.w3.org/2001/XMLSchema-instance}schemaLocation" -> "http://www.accellera.org/XMLSchema/IPXACT/1685-2022 http://www.accellera.org/XMLSchema/IPXACT/1685-2022/index.xsd"
//  )

  val ktsAttributes = KtsAttributes(kts_productHier = "Flat", kts_implementation = "HW", kts_firmness = "Mutable")
  val kactus2Attributes = Kactus2Attributes(kts_attributes = ktsAttributes)
  val attributes = Attributes(kactus2_attributes = kactus2Attributes)

  val component = DesignComponent(
    vendor = "HWDesign",
    library = "flat",
    name = "Design1",
    version = BigDecimal(1.0),
    model = model,
    attributes = attributes
  )

  // 自定义生成 XML 的函数



  // 生成 XML
  val xml = designComponentToXml(component)

  val xmlString = """<?xml version="1.0" encoding="UTF-8"?>"""+"\n"+Utility.serialize(xml)

  // 将格式化后的 XML 写入文件
  val basePath = Paths.get(".", "Kactus2","HWDesign", "flat", "Dsign1", "1.0")
  Files.createDirectories(basePath) // 确保目录存在
  val filePath = basePath.resolve("Dsign1.1.0.xml").toString
  val writer = new PrintWriter(new File(filePath))
  writer.write(xmlString.toString)
  writer.close()
}
