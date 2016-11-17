var companiesSelected;
var companiesJson;
var projectsSelected;
var membersSelected;
var startingDate = moment().subtract(29, 'days');
var endingDate = moment();

$.getJSON("companies/json", function(data) {
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
           'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment()],
           "I Release 1.8.0 [current]": [{ y:2016, M:9, d:26}, moment()],
           "H Release 1.7.1": [{ y:2016, M:8, d:24}, { y:2016, M:9, d:25}],
           "H Release 1.7.0": [{ y:2016, M:5, d:25}, { y:2016, M:8, d:23}],
           "G Release 1.6.0": [{ y:2016, M:3, d:21}, { y:2016, M:5, d:24}],
           "F Release 1.5.1": [{ y:2016, M:2, d:11}, { y:2016, M:3, d:20}],
           "F Release 1.5.0": [{ y:2015, M:11, d:17}, { y:2016, M:2, d:10}],
           "E Release 1.4.0": [{ y:2015, M:8, d:19}, { y:2015, M:11, d:16}]
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
             value: value[0],
             text: value[1],
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