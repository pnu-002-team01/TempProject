<?php
$filename = $_POST[source];
$myfile = fopen("./cppsource/".$filename.".cpp","w") or die("Unable to open file!");
$txt = $_POST[content];
fwrite($myfile,$txt);
fclose($myfile);
$output;
$output=shell_exec("python ./cpplint-master/cpplint.py ./cppsource/".$filename.".cpp 2>&1");
echo "<pre>$output</pre>";
$write = fopen("./result/".$filename.".txt","w");
fwrite($write, $output);
fclose($write);
?>
