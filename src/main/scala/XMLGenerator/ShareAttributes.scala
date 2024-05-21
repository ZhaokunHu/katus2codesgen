package XMLGenerator

case class Attributes(kactus2_attributes: Kactus2Attributes)

case class Kactus2Attributes(kts_attributes: KtsAttributes)

case class KtsAttributes(kts_productHier: String, kts_implementation: String, kts_firmness: String)

// 定义数据类


case class VendorExtensions(kactus2_version: String)

case class Ports(port: Seq[Port])

case class Port(name: String,
                wire: Wire,
                generateVendorExtensions: Boolean = false)


case class Wire(direction: String,
                vectors: Vectors)

case class Vectors(vector: Vector)

case class Vector(left: BigInt,
                  right: BigInt)




case class Views(view: View)

case class View(name: String,
                designConfigurationInstantiationRef: String)

case class Instantiations(designConfigurationInstantiation: DesignConfigurationInstantiation)

case class DesignConfigurationInstantiation(name: String,
                                            designConfigurationRef: DesignConfigurationRef)

case class DesignConfigurationRef(attributes: Map[String, String] = Map.empty) {
  lazy val library: String = attributes("@library")
  lazy val name: String = attributes("@name")
  lazy val vendor: String = attributes("@vendor")
  lazy val version: BigDecimal = BigDecimal(attributes("@version"))
}

case class ChildrenModel(ports: Ports,
                         vendorExtensions: VendorExtensions)
case class ChildrenComponent(vendor: String,
                             library: String,
                             name: String,
                             version: BigDecimal,
                             model: ChildrenModel,
                             attributes: Attributes) {
  lazy val xsischemaLocation: String = "http://www.accellera.org/XMLSchema/IPXACT/1685-2022 http://www.accellera.org/XMLSchema/IPXACT/1685-2022/index.xsd"
}
case class DesignModel  (views: Views, instantiations: Instantiations,ports: Ports,
                         vendorExtensions: VendorExtensions)

// 定义数据类
case class DesignComponent(vendor: String,
                     library: String,
                     name: String,
                     version: BigDecimal,
                     model: DesignModel,
                     attributes: Attributes) {
  lazy val xsischemaLocation: String = "http://www.accellera.org/XMLSchema/IPXACT/1685-2022 http://www.accellera.org/XMLSchema/IPXACT/1685-2022/index.xsd"
}

//下面是config的定义
case class DesignRef(vendor: String, library: String, name: String, version: String)

case class Kactus2AttributesImplementation(kts_implementation: String)

case class VendorExtensionsWithAttributes(version: String, kts_attributes: Kactus2AttributesImplementation)

case class DesignConfiguration(vendor: String,
                               library: String,
                               name: String,
                               version: String,
                               designRef: DesignRef,
                               vendorExtensions: VendorExtensionsWithAttributes) {
  lazy val xsischemaLocation: String = "http://www.accellera.org/XMLSchema/IPXACT/1685-2022 http://www.accellera.org/XMLSchema/IPXACT/1685-2022/index.xsd"
}

//下边是design的实现
case class ComponentRef(vendor: String, library: String, name: String, version: String)
case class ComponentInstance(instanceName: String, componentRef: ComponentRef)
case class PortReference(componentInstanceRef: String, portRef: String)
case class AdHocConnection(name: String, portReferences: Seq[PortReference])
case class Design(vendor: String, library: String, name: String, version: String,
                  componentInstances: Seq[ComponentInstance], adHocConnections: Seq[AdHocConnection])

