<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office"
          xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:html="http://www.w3.org/TR/REC-html40" xmlns:dt="uuid:C2F41010-65B3-11d1-A29F-00AA00C14882">
    <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <Author>Administrator</Author>
        <LastAuthor>Not stingy.</LastAuthor>
        <Created>2023-01-17T07:59:00Z</Created>
        <LastSaved>2023-01-17T08:07:55Z</LastSaved>
    </DocumentProperties>
    <CustomDocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <ICV dt:dt="string">AFDE85CCF2114432A6AB3B36939FF63A</ICV>
        <KSOProductBuildVer dt:dt="string">2052-11.1.0.13703</KSOProductBuildVer>
    </CustomDocumentProperties>
    <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
        <WindowWidth>28125</WindowWidth>
        <WindowHeight>12465</WindowHeight>
        <ProtectStructure>False</ProtectStructure>
        <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
            <Alignment ss:Vertical="Center"/>
            <Borders/>
            <Font ss:FontName="宋体" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
            <Interior/>
            <NumberFormat/>
            <Protection/>
        </Style>
        <Style ss:ID="s49"/>
        <Style ss:ID="s50">
            <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
        </Style>
    </Styles>
    <Worksheet ss:Name="Sheet1">
        <Table ss:ExpandedColumnCount="1" ss:ExpandedRowCount="2" x:FullColumns="1" x:FullRows="1"
               ss:DefaultColumnWidth="54" ss:DefaultRowHeight="13.5">
            <Column ss:Index="1" ss:StyleID="Default" ss:AutoFitWidth="0" ss:Width="84"/>
            <Row ss:Height="41">
                <Cell ss:StyleID="s50" ss:MergeAcross="${treeDeep}">
                    <Data ss:Type="String">评议项</Data>
                </Cell>
            </Row>
            <#list tree as item>
                <Row ss:Height="27">
                    <Cell ss:StyleID="s50" ss:MergeDown="${item.floorsNum-1}">
                        <Data ss:Type="String">${item.evalName}</Data>
                    </Cell>
                </Row>
                <#if (item.floorsNum-2) gte 0>
                    <#list 0..(item.floorsNum-2) as i>
                        <Row ss:Height="27"></Row>
                    </#list>
                </#if>
            </#list>
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Left="0.7" x:Right="0.7" x:Top="0.75" x:Bottom="0.75"/>
            </PageSetup>
            <Selected/>
            <TopRowVisible>0</TopRowVisible>
            <LeftColumnVisible>0</LeftColumnVisible>
            <PageBreakZoom>100</PageBreakZoom>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
    <Worksheet ss:Name="Sheet2">
        <Table ss:ExpandedColumnCount="1" ss:ExpandedRowCount="1" x:FullColumns="1" x:FullRows="1"
               ss:DefaultColumnWidth="54" ss:DefaultRowHeight="13.5"/>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Left="0.7" x:Right="0.7" x:Top="0.75" x:Bottom="0.75"/>
            </PageSetup>
            <TopRowVisible>0</TopRowVisible>
            <LeftColumnVisible>0</LeftColumnVisible>
            <PageBreakZoom>100</PageBreakZoom>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
    <Worksheet ss:Name="Sheet3">
        <Table ss:ExpandedColumnCount="1" ss:ExpandedRowCount="1" x:FullColumns="1" x:FullRows="1"
               ss:DefaultColumnWidth="54" ss:DefaultRowHeight="13.5"/>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Left="0.7" x:Right="0.7" x:Top="0.75" x:Bottom="0.75"/>
            </PageSetup>
            <TopRowVisible>0</TopRowVisible>
            <LeftColumnVisible>0</LeftColumnVisible>
            <PageBreakZoom>100</PageBreakZoom>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
</Workbook>