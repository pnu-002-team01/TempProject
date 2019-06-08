<?php
$filename = $_POST[source];
$filename = $filename.".cpp";
echo $filename;
$myfile = fopen($filename,"w") or die("Unable to open file!");
$txt = $_POST[content];
fwrite($myfile,$txt);
fclose($myfile);
$output;
$output=shell_exec("python /home/kimjaehoon/cpplint-master/cpplint.py ".$filename." 2>&1");
echo "<pre>$output</pre>";
?>