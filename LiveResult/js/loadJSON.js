// $(document).ready(function() {
//     $.getJSON("./jsonData.json", function(data) {


//         //Storing total test count
//         document.getElementById("TotalTestCount").innerHTML = data.metadata.TotalTest;

//         console.log(data.Result.length);

//         //Storing Executed test count 
//         var iExecutedCounter = 0;
//         for (var i = 0; i < data.Result.length; i++) {

//             if (data.Result[i].Result != "Started") {
//                 iExecutedCounter++;
//             }
//         }
//         document.getElementById("ExecutedCount").innerHTML = "" + iExecutedCounter;

//         //Storing Executed test count 
//         var iPassCounter = 0;
//         for (var i = 0; i < data.Result.length; i++) {

//             if (data.Result[i].Result == "Pass") {
//                 iPassCounter++;
//             }
//         }
//         document.getElementById("PassedCount").innerHTML = "" + iPassCounter;

//         //Storing Executed Fail count 
//         var iFailCounter = 0;
//         for (var i = 0; i < data.Result.length; i++) {

//             if (data.Result[i].Result == "Fail") {
//                 iFailCounter++;
//             }
//         }
//         document.getElementById("FailedCount").innerHTML = "" + iFailCounter;

//         //Storing Executed Skip count 
//         var iSkipCounter = 0;
//         for (var i = 0; i < data.Result.length; i++) {

//             if (data.Result[i].Result == "Skip") {
//                 iSkipCounter++;
//             }
//         }
//         document.getElementById("SkippedCount").innerHTML = "" + iSkipCounter;

//     });
// });




// // function parseJSONToTable(data) {

// //     var testResultTable = document.getElementById("TestResult");

// //     for (var i = 0; i < data.Result.length; i++) {
// //         var createRow = '<tr><td>${(i+1)}</td>  <td>data.Result[i].Suite</td>   <td>data.Result[i].Test</td>   <td>data.Result[i].TestName</td>   <td>data.Result[i].Result</td>   <td>data.Result[i].Error</td>   <td>data.Result[i].TimeDuration</td></tr>'
// //         testResultTable += createRow;
// //     }


// // }

// // function storeRunInformation(data) {

// // }


(function() {

    //Storing total test count
    document.getElementById("TotalTestCount").innerHTML = data.metadata.TotalTest;

    console.log(data.Result.length);

    //Storing Executed test count 
    var iExecutedCounter = 0;
    for (var i = 0; i < data.Result.length; i++) {

        if (data.Result[i].Result != "Started") {
            iExecutedCounter++;
        }
    }
    document.getElementById("ExecutedCount").innerHTML = "" + iExecutedCounter;

    //Storing Executed test count 
    var iPassCounter = 0;
    for (var i = 0; i < data.Result.length; i++) {

        if (data.Result[i].Result == "Pass") {
            iPassCounter++;
        }
    }
    document.getElementById("PassedCount").innerHTML = "" + iPassCounter;

    //Storing Executed Fail count 
    var iFailCounter = 0;
    for (var i = 0; i < data.Result.length; i++) {

        if (data.Result[i].Result == "Fail") {
            iFailCounter++;
        }
    }
    document.getElementById("FailedCount").innerHTML = "" + iFailCounter;

    //Storing Executed Skip count 
    var iSkipCounter = 0;
    for (var i = 0; i < data.Result.length; i++) {

        if (data.Result[i].Result == "Skip") {
            iSkipCounter++;
        }
    }
    document.getElementById("SkippedCount").innerHTML = "" + iSkipCounter;

})();


(function() {

    var testResultTable = document.getElementById("TestResult");

    // var tableBody = document.createElement("body");
    // testResultTable.appendChild(tableBody);


    for (var i = 0; i < data.Result.length; i++) {


        var tableRow = document.createElement("tr");
        testResultTable.appendChild(tableRow);

        //Sl No
        var tdSlNo = document.createElement("td");
        var tdSlNoText = document.createTextNode(i + 1);
        tdSlNo.appendChild(tdSlNoText);
        tableRow.appendChild(tdSlNo);


        //Suite
        var tdSuite = document.createElement("td");
        var tdSuiteText = document.createTextNode(data.Result[i].Suite);
        tdSuite.appendChild(tdSuiteText);
        tableRow.appendChild(tdSuite);


        //Test
        var tdTest = document.createElement("td");
        var tdTestText = document.createTextNode(data.Result[i].Test);
        tdTest.appendChild(tdTestText);
        tableRow.appendChild(tdTest);

        //TestName
        var tdTestName = document.createElement("td");
        var tdTestNameText = document.createTextNode(data.Result[i].TestName);
        tdTestName.appendChild(tdTestNameText);
        tableRow.appendChild(tdTestName);


        //Result
        var tdResult = document.createElement("td");
        var tdResultText = document.createTextNode(data.Result[i].Result);
        tdResult.appendChild(tdResultText);
        tableRow.appendChild(tdResult);


        //Error
        var tdError = document.createElement("td");
        var tdErrorText = document.createTextNode(data.Result[i].Error);
        tdError.appendChild(tdErrorText);
        tableRow.appendChild(tdError);

        //Error
        var tdTimeDuration = document.createElement("td");
        var tdTimeDurationText = document.createTextNode(data.Result[i].TimeDuration + " seconds");
        tdTimeDuration.appendChild(tdTimeDurationText);
        tableRow.appendChild(tdTimeDuration);

    }


})();