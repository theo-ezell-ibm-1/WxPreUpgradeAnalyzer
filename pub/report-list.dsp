<!DOCTYPE html>
<html>
	<head>
		<title>Report List</title>
		<link rel="stylesheet" type="text/css" href="webMethods.css"/>
		<link rel="stylesheet" type="text/css" href="wxpreupgradeanalyzer.css"/>
		<script language="JavaScript">
			function toggle(source) {
			  checkboxes = document.getElementsByName('files');
			  for(let checkbox of checkboxes){
				checkbox.checked = source.checked;
			  }
			}
		</script>
	</head>
    <body class="body">
	%ifvar action equals('delete')%
		%invoke WxPreUpgradeAnalyzer.ui:deleteReports%
			%ifvar succeeded -notempty%
				<p class="message">The following files were successfully deleted: %nl%
				%loop succeeded%%value succeeded%%loopsep ', '%%endloop%</p>
			%endif%
			%ifvar failed%
				<p class="error">The following files could not be deleted: <p>
				%loop failed%%value failed%%loopsep ', '%%endloop%</p>
			%endif%
		%endinvoke%
	%endif%
	<form name="testform" method="post" action="report-list.dsp" target="body">
		<table width="100%">
			<tbody>
				<tr>
					<td>
						<table class="tableView" cellpadding="2"> 
							<tbody>
								<tr>
									<td class="heading" colspan="2">WxPreUpgradeAnalyzer Reports &nbsp;</td>
									%invoke WxPreUpgradeAnalyzer.ui:listReports%
									%ifvar numFiles equals('0')%
									<tr>
										<td class="td" class="oddcol-l">Reports directory is empty.</td>
									</tr>
									%else%
									<tr class="subheading">
										<th class="oddcol"><input title="Select / Unselect all" type="checkbox" onclick="toggle(this);"/></th>
										<th class="evencol-l">File name</th>
									</tr>
									%loop fileList%
									<tr>
										<td class="oddcol"><input type="checkbox" name="files" value="%value fileList%"></td><td class="evencol-l"><a href="reports/%value fileList%" download="reports/%value fileList%" class="td">%value fileList%</a></td>
									</tr>
									%endloop%
									<tr>
										<td class="action" colspan="2"><input name="action" type="hidden" value="delete"><input name="delButton" class="button2" type="submit" value="Delete Selected Reports" onclick="return confirm('Are you sure you want to delete the selected file(s)?');"></td>
									</tr>
									%endif%
								</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>