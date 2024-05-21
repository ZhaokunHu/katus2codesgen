package XMLGenerator

import java.io.{File, PrintWriter}
import scala.xml.{Elem, Utility}

object SharedXMLGenerator {
  def viewsToXml(views: Views): Elem = {
    <ipxact:views>{viewToXml(views.view)}</ipxact:views>
  }

  def viewToXml(view: View): Elem = {
    <ipxact:view>
      <ipxact:name>{view.name}</ipxact:name>
      <ipxact:designConfigurationInstantiationRef>{view.designConfigurationInstantiationRef}</ipxact:designConfigurationInstantiationRef>
    </ipxact:view>
  }

  def instantiationsToXml(instantiations: Instantiations): Elem = {
    <ipxact:instantiations>
      {instantiationToXml(instantiations.designConfigurationInstantiation)}
    </ipxact:instantiations>
  }

  def instantiationToXml(instantiation: DesignConfigurationInstantiation): Elem = {
    <ipxact:designConfigurationInstantiation>
      <ipxact:name>{instantiation.name}</ipxact:name>
      {designConfigurationRefToXml(instantiation.designConfigurationRef)}
    </ipxact:designConfigurationInstantiation>
  }

  def designConfigurationRefToXml(ref: DesignConfigurationRef): Elem = {
      <ipxact:designConfigurationRef vendor={ref.vendor} library={ref.library} name={ref.name} version={ref.version.toString}/>
  }

  def portsToXml(ports: Ports): Elem = {
    <ipxact:ports>
      {ports.port.map(portToXml)}
    </ipxact:ports>
  }

  def portToXml(port: Port): Elem = {
    val vendorExtensions = if (port.generateVendorExtensions) {
      <ipxact:vendorExtensions><kactus2:adHocVisible/>
      </ipxact:vendorExtensions>
    } else {
      Nil
    }

    <ipxact:port>
      <ipxact:name>{port.name}</ipxact:name>
      {wireToXml(port.wire)}{vendorExtensions}
    </ipxact:port>
  }

  def wireToXml(wire: Wire): Elem = {
    <ipxact:wire>
      <ipxact:direction>{wire.direction}</ipxact:direction>
      {vectorsToXml(wire.vectors)}
    </ipxact:wire>
  }

  def vectorsToXml(vectors: Vectors): Elem = {
    <ipxact:vectors>
      {vectorToXml(vectors.vector)}
    </ipxact:vectors>
  }

  def vectorToXml(vector: Vector): Elem = {
    <ipxact:vector><ipxact:left>{vector.left}</ipxact:left><ipxact:right>{vector.right}</ipxact:right></ipxact:vector>
  }

  def childrenComponentToXml(component: ChildrenComponent): Elem = {
    <ipxact:component xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xmlns:ipxact="http://www.accellera.org/XMLSchema/IPXACT/1685-2022"
                      xmlns:kactus2="http://kactus2.cs.tut.fi"
                      xsi:schemaLocation={component.xsischemaLocation}>
      <ipxact:vendor>{component.vendor}</ipxact:vendor>
      <ipxact:library>{component.library}</ipxact:library>
      <ipxact:name>{component.name}</ipxact:name>
      <ipxact:version>{component.version}</ipxact:version>
      {childrenModelToXml(component.model)}
      <ipxact:vendorExtensions>
        <kactus2:kts_attributes>
          <kactus2:kts_productHier>{component.attributes.kactus2_attributes.kts_attributes.kts_productHier}</kactus2:kts_productHier>
          <kactus2:kts_implementation>{component.attributes.kactus2_attributes.kts_attributes.kts_implementation}</kactus2:kts_implementation>
          <kactus2:kts_firmness>{component.attributes.kactus2_attributes.kts_attributes.kts_firmness}</kactus2:kts_firmness>
        </kactus2:kts_attributes>
        <kactus2:version>{component.model.vendorExtensions.kactus2_version}</kactus2:version>
      </ipxact:vendorExtensions>
    </ipxact:component>
  }

  def childrenModelToXml(model: ChildrenModel): Elem = {
    <ipxact:model>
      {portsToXml(model.ports)}
    </ipxact:model>
  }

  def designComponentToXml(component: DesignComponent): Elem = {
    <ipxact:component xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xmlns:ipxact="http://www.accellera.org/XMLSchema/IPXACT/1685-2022"
                      xmlns:kactus2="http://kactus2.cs.tut.fi"
                      xsi:schemaLocation={component.xsischemaLocation}>
      <ipxact:vendor>{component.vendor}</ipxact:vendor>
      <ipxact:library>{component.library}</ipxact:library>
      <ipxact:name>{component.name}</ipxact:name>
      <ipxact:version>{component.version}</ipxact:version>
      {designModelToXml(component.model)}
      <ipxact:vendorExtensions>
      <kactus2:kts_attributes>
        <kactus2:kts_productHier>{component.attributes.kactus2_attributes.kts_attributes.kts_productHier}</kactus2:kts_productHier>
        <kactus2:kts_implementation>{component.attributes.kactus2_attributes.kts_attributes.kts_implementation}</kactus2:kts_implementation>
        <kactus2:kts_firmness>{component.attributes.kactus2_attributes.kts_attributes.kts_firmness}</kactus2:kts_firmness>
      </kactus2:kts_attributes>
      <kactus2:version>{component.model.vendorExtensions.kactus2_version}</kactus2:version>
    </ipxact:vendorExtensions>
    </ipxact:component>
  }

  def designModelToXml(model: DesignModel): Elem = {
    <ipxact:model>
      {viewsToXml(model.views)}
      {instantiationsToXml(model.instantiations)}
      {portsToXml(model.ports)}
    </ipxact:model>
  }


  def designConfigurationToXml(config: DesignConfiguration): Elem = {
    <ipxact:designConfiguration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                xmlns:ipxact="http://www.accellera.org/XMLSchema/IPXACT/1685-2022"
                                xmlns:kactus2="http://kactus2.cs.tut.fi"
                                xsi:schemaLocation={config.xsischemaLocation}>
      <ipxact:vendor>{config.vendor}</ipxact:vendor>
      <ipxact:library>{config.library}</ipxact:library>
      <ipxact:name>{config.name}</ipxact:name>
      <ipxact:version>{config.version}</ipxact:version>
      {designRefToXml(config.designRef)}
      {vendorExtensionsToXml(config.vendorExtensions)}
    </ipxact:designConfiguration>
  }

  def designRefToXml(ref: DesignRef): Elem = {
      <ipxact:designRef vendor={ref.vendor} library={ref.library} name={ref.name} version={ref.version}/>
  }

  def vendorExtensionsToXml(extensions: VendorExtensionsWithAttributes): Elem = {
    <ipxact:vendorExtensions>
      <kactus2:version>{extensions.version}</kactus2:version>
      {ktsAttributesToXml(extensions.kts_attributes)}
    </ipxact:vendorExtensions>
  }

  def ktsAttributesToXml(attributes: Kactus2AttributesImplementation): Elem = {
    <kactus2:kts_attributes>
      <kactus2:kts_implementation>{attributes.kts_implementation}</kactus2:kts_implementation>
    </kactus2:kts_attributes>
  }

//下边是design的实现
def componentRefToXml(ref: ComponentRef): Elem = {
    <ipxact:componentRef vendor={ref.vendor} library={ref.library} name={ref.name} version={ref.version}/>
}

  def componentInstanceToXml(instance: ComponentInstance): Elem = {
    <ipxact:componentInstance>
      <ipxact:instanceName>{instance.instanceName}</ipxact:instanceName>
      {componentRefToXml(instance.componentRef)}
    </ipxact:componentInstance>
  }


  def adHocConnectionToXml(connection: AdHocConnection): Elem = {
    <ipxact:adHocConnection>
      <ipxact:name>{connection.name}</ipxact:name>
      <ipxact:portReferences>
        {connection.portReferences.map {
        case PortReference(instanceRef, portRef) if instanceRef.nonEmpty =>
            <ipxact:internalPortReference componentInstanceRef={instanceRef} portRef={portRef}/>
        case PortReference(_, portRef) =>
            <ipxact:externalPortReference portRef={portRef}/>
      }}
      </ipxact:portReferences>
    </ipxact:adHocConnection>
  }

  def designToXml(design: Design): Elem = {
    <ipxact:design xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns:ipxact="http://www.accellera.org/XMLSchema/IPXACT/1685-2022"
                   xmlns:kactus2="http://kactus2.cs.tut.fi"
                   xsi:schemaLocation="http://www.accellera.org/XMLSchema/IPXACT/1685-2022 http://www.accellera.org/XMLSchema/IPXACT/1685-2022/index.xsd">
      <ipxact:vendor>{design.vendor}</ipxact:vendor>
      <ipxact:library>{design.library}</ipxact:library>
      <ipxact:name>{design.name}</ipxact:name>
      <ipxact:version>{design.version}</ipxact:version>
      <ipxact:componentInstances>
        {design.componentInstances.map(componentInstanceToXml)}
      </ipxact:componentInstances>
      <ipxact:adHocConnections>
        {design.adHocConnections.map(adHocConnectionToXml)}
      </ipxact:adHocConnections>
    </ipxact:design>
  }
}
