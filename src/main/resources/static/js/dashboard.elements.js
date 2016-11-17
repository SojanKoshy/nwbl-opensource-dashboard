var companiesSelected;
var companiesJson;
var projectsSelected;
var membersSelected;
var startingDate = moment().subtract(29, 'days');
var endingDate = moment();

$.getJSON("company/json", function(data) {
    companiesJson = data;
    rebuildMemberMultiSelectDropDown();
});

function activateDateRangePicker() {
    function cb(start, end) {
        $('#date span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        startingDate = start;
        endingDate = end;
        drawDashboardCharts();
    }

    $('#date').daterangepicker({
        startDate: startingDate,
        endDate: endingDate,
        opens: "center",
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

function activateCompanyMultiSelectDropDown() {
    function onChangeCompanies(value) {
        companiesSelected = value;
        rebuildMemberMultiSelectDropDown();
        drawDashboardCharts();
    }
    $('#companies').multiselect({
        includeSelectAllOption: true,
        selectAllText: 'All Companies',
        enableFiltering: true,
        enableCaseInsensitiveFiltering: true,
        maxHeight: 300,
        numberDisplayed: 1,
        onInitialized: function() {companiesSelected = this.$select.val();},
        onChange: function() {onChangeCompanies(this.$select.val());},
        onSelectAll: function() {onChangeCompanies(this.$select.val());},
        onDeselectAll: function() {onChangeCompanies(this.$select.val());}
    });
}

function activateMemberMultiSelectDropDown() {
    function onChangeMembers(value) {
        membersSelected = value;
        drawDashboardCharts();
    }

    $('#members').multiselect({
        includeSelectAllOption: true,
        selectAllText: 'All Members',
        enableFiltering: true,
        enableCaseInsensitiveFiltering: true,
        maxHeight: 300,
        numberDisplayed: 1,
        onInitialized: function() {membersSelected = this.$select.val();},
        onChange: function() {onChangeMembers(this.$select.val());},
        onSelectAll: function() {onChangeMembers(this.$select.val());},
        onDeselectAll: function() {onChangeMembers(this.$select.val());}
    });
}

function rebuildMemberMultiSelectDropDown() {
    var vals = [];

    $.each(companiesSelected, function(index, value) {
        vals = vals.concat(companiesJson[parseInt(value)]);
    });

    var $members = $("#members");
    $members.empty();
    vals.sort();
    $.each(vals, function(index, value) {
        $('#members').append($('<option>', {
             value: value,
             text: value,
             selected: 'selected'
         }));
    });

    $('#members').multiselect('rebuild');
}

function activateProjectMultiSelectDropDown() {
    function onChangeProjects(value) {
        projectsSelected = value;
        drawDashboardCharts();
    }
    $('#projects').multiselect({
        includeSelectAllOption: true,
        selectAllText: 'All Projects',
        enableFiltering: true,
        enableCaseInsensitiveFiltering: true,
        maxHeight: 300,
        numberDisplayed: 1,
        onInitialized: function() {projectsSelected = this.$select.val();},
        onChange: function() {onChangeProjects(this.$select.val());},
        onSelectAll: function() {onChangeProjects(this.$select.val());},
        onDeselectAll: function() {onChangeProjects(this.$select.val());}
    });
}


function activateDownload() {
    $("#download").click(function () {
        var params = startingDate.format('YYYY-MM-DD/') + endingDate.format('YYYY-MM-DD/') + projectsSelected
        location = "download/" + params;
    });
}