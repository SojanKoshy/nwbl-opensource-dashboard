var projectsSelected;
var startingDate = moment().subtract(29, 'days');
var endingDate = moment();

function activateDateRangePicker() {
    function cb(start, end) {
        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        startingDate = start;
        endingDate = end;
        drawDashboardCharts();
    }

    $('#reportrange').daterangepicker({
        showDropdowns: true,
        startDate: startingDate,
        endDate: endingDate,
        ranges: {
           'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
           'Last 7 Days': [moment().subtract(6, 'days'), moment()],
           'Last 30 Days': [moment().subtract(29, 'days'), moment()],
           'This Month': [moment().startOf('month'), moment().endOf('month')],
           'Last 3 Months': [moment().subtract(3, 'month').startOf('month'), moment()],
           'Last 6 Months': [moment().subtract(6, 'month').startOf('month'), moment()],
           'This Year': [moment().startOf('year'), moment().endOf('month')],
           'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment()]
        }
    }, cb);

    cb(startingDate, endingDate);
}

function activateProjectMultiSelectDropDown() {
    function onChangeProjects(value) {
        projectsSelected = value;
        drawDashboardCharts();
    }
    $('#projectsOption').multiselect({
        includeSelectAllOption: true,
        selectAllText: 'All Projects',
        onInitialized: function() {projectsSelected = this.$select.val();},
        onChange: function() {onChangeProjects(this.$select.val());},
        onSelectAll: function() {onChangeProjects(this.$select.val());},
        onDeselectAll: function() {onChangeProjects(this.$select.val());}
    });
}

function activateDownload() {
    $("#downloadButton").click(function () {
        var params = startingDate.format('YYYY-MM-DD/') + endingDate.format('YYYY-MM-DD/') + projectsSelected
        window.location = "chart/download/" + params;
    });
}