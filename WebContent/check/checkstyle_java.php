</php
$filename = $_POST[source];
$company = $_POST[company];
echo $company;
$company = $company."_checks.xml";
$filename = $filename.".java";
$myfile = fopen($filename,"w") or die("Unable to open file!");
$txt = $_POST[content];
fwrite($myfile,$txt);
fclose($myfile);
$output = shell_exec("checkstyle -c /home/kimjaehoon/checkstyle/".$company." ".$filename." 2>&1");
echo "<pre>$output</pre>";
?>