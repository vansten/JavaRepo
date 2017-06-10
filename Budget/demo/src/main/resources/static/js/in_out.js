$jq1(document).ready(function()
    {
        $jq1("#in_out_table").tablesorter({
                widthFixed : true,
                dateFormat : "ddmmyyyy",
                sortInitialOrder : "asc",
                cancelSelection : true,
                debug : false
        });
    }
)