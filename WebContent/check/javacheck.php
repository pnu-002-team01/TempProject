<?php
$filename = $_POST[source];
$company = $_POST[company];
$company = $company."_checks.xml";
$myfile = fopen("./javasource/".$filename.".java","w") or die("Unable to open file!");
$txt = $_POST[content];
fwrite($myfile, $txt);
fclose($myfile);
$output = shell_exec("checkstyle -c ./javarule/".$company." ./javasource/".$filename.".java 2>&1");
echo "<pre>$output</pre>";
$write = fopen("./result/".$filename.".txt","w");
fwrite($write, $output);
fclose($write);
?>
