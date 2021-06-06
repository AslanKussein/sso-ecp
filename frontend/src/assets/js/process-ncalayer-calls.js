var g_callback = null;

function getActiveTokensCall() {
  // blockScreen();
  getActiveTokens("getActiveTokensBack");
}

function getActiveTokensBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var listOfTokens = result['responseObject'];
    $('#storageSelect').empty();
    $('#storageSelect').append('<option value="(">PKCS12</option>');
    for (var i = 0; i < listOfTokens.length; i++) {
      $('#storageSelect').append('<option value="' + listOfTokens[i] + '">' + listOfTokens[i] + '</option>');
    }
  }
}

function getKeyInfoCall() {
  // blockScreen();
  var selectedStorage = $('#storageSelect').val();
  getKeyInfo(selectedStorage, "getKeyInfoBack");
}

function getKeyInfoBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];

    var alias = res['alias'];
    $("#alias").val(alias);

    var keyId = res['keyId'];
    $("#keyId").val(keyId);

    var algorithm = res['algorithm'];
    $("#algorithm").val(algorithm);

    var subjectCn = res['subjectCn'];
    $("#subjectCn").val(subjectCn);

    var subjectDn = res['subjectDn'];
    $("#subjectDn").val(subjectDn);

    var issuerCn = res['issuerCn'];
    $("#issuerCn").val(issuerCn);

    var issuerDn = res['issuerDn'];
    $("#issuerDn").val(issuerDn);

    var serialNumber = res['serialNumber'];
    $("#serialNumber").val(serialNumber);

    var dateString = res['certNotAfter'];
    var date = new Date(Number(dateString));
    $("#notafter").val(date.toLocaleString());

    dateString = res['certNotBefore'];
    date = new Date(Number(dateString));
    $("#notbefore").val(date.toLocaleString());

    var authorityKeyIdentifier = res['authorityKeyIdentifier'];
    $("#authorityKeyIdentifier").val(authorityKeyIdentifier);

    var pem = res['pem'];
    $("#pem").val(pem);
  }
}

function callSignXml(callBack) {
  g_callback = callBack;
  console.log(callBack)
  /**
   * CONNECTING   0   The connection is not yet open.
   * OPEN         1   The connection is open and ready to communicate.
   * CLOSING      2   The connection is in the process of closing.
   * CLOSED       3   The connection is closed or couldn't be opened.
   */
  if (webSocket === null || webSocket.readyState === 3 || webSocket.readyState === 2) {
    initNCALayer('signXmlCall');
  } else {
    signXmlCall();
  }
}

function signXmlCall() {
  var xmlToSign = '<?xml version="1.0" encoding="utf-8"?><test>1234567</test>';
  var selectedStorage = "PKCS12";
  // blockScreen();
  signXml(selectedStorage, "AUTHENTICATION", xmlToSign, "signXmlBack");
}

function signXmlBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    $("#certificate").val('');
    // alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#certificate").val(res);
    g_callback();
  }
}

function signXmlsCall() {
  var xmlToSign1 = $("#xmlToSign1").val();
  var xmlToSign2 = $("#xmlToSign2").val();
  var xmlsToSign = new Array(xmlToSign1, xmlToSign2);
  var selectedStorage = $('#storageSelect').val();
  // blockScreen();
  signXmls(selectedStorage, "SIGNATURE", xmlsToSign, "signXmlsBack");
}

function signXmlsBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#signedXml1").val(res[0]);
    $("#signedXml2").val(res[1]);
  }
}

function createCAdESFromFileCall() {
  var selectedStorage = $('#storageSelect').val();
  var flag = $("#flag").is(':checked');
  var filePath = $("#filePath").val();
  if (filePath !== null && filePath !== "") {
    // blockScreen();
    createCAdESFromFile(selectedStorage, "SIGNATURE", filePath, flag, "createCAdESFromFileBack");
  } else {
    alert("Не выбран файл для подписи!");
  }
}

function createCAdESFromFileBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#createdCMS").val(res);
  }
}

function createCAdESFromBase64Call() {
  var selectedStorage = $('#storageSelect').val();
  var flag = $("#flagForBase64").is(':checked');
  var base64ToSign = $("#base64ToSign").val();
  if (base64ToSign !== null && base64ToSign !== "") {
    $.blockUI();
    createCAdESFromBase64(selectedStorage, "SIGNATURE", base64ToSign, flag, "createCAdESFromBase64Back");
  } else {
    alert("Нет данных для подписи!");
  }
}

function createCAdESFromBase64Back(result) {
  $.unblockUI();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#createdCMSforBase64").val(res);
  }
}

function createCAdESFromBase64HashCall() {
  var selectedStorage = $('#storageSelect').val();
  var base64ToSign = $("#base64HashToSign").val();
  if (base64ToSign !== null && base64ToSign !== "") {
    $.blockUI();
    createCAdESFromBase64Hash(selectedStorage, "SIGNATURE", base64ToSign, "createCAdESFromBase64HashBack");
  } else {
    alert("Нет данных для подписи!");
  }
}

function createCAdESFromBase64HashBack(result) {
  $.unblockUI();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#createdCMSforBase64Hash").val(res);
  }
}

function applyCAdESTCall() {
  var selectedStorage = $('#storageSelect').val();
  var cmsForTS = $("#CMSForTS").val();
  if (cmsForTS !== null && cmsForTS !== "") {
    $.blockUI();
    applyCAdEST(selectedStorage, "SIGNATURE", cmsForTS, "applyCAdESTBack");
  } else {
    alert("Нет данных для подписи!");
  }
}

function applyCAdESTBack(result) {
  $.unblockUI();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#createdCMSWithAppliedTS").val(res);
  }
}

function showFileChooserCall() {
  // blockScreen();
  showFileChooser("ALL", "", "showFileChooserBack");
}

function showFileChooserBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#filePath").val(res);
  }
}

function showFileChooserForTSCall() {
  // blockScreen();
  showFileChooser("ALL", "", "showFileChooserForTSBack");
}

function showFileChooserForTSBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#filePathWithTS").val(res);
  }
}

function changeLocaleCall() {
  var selectedLocale = $('#localeSelect').val();
  changeLocale(selectedLocale);
}

function createCMSSignatureFromFileCall() {
  var selectedStorage = $('#storageSelect').val();
  var flag = $("#flagForCMSWithTS").is(':checked');
  var filePath = $("#filePathWithTS").val();
  if (filePath !== null && filePath !== "") {
    // blockScreen();
    createCMSSignatureFromFile(selectedStorage, "SIGNATURE", filePath, flag, "createCMSSignatureFromFileBack");
  } else {
    alert("Не выбран файл для подписи!");
  }
}

function createCMSSignatureFromFileBack(result) {
  // unblockScreen();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#createdCMSWithTS").val(res);
  }
}

function createCMSSignatureFromBase64Call() {
  var selectedStorage = $('#storageSelect').val();
  var flag = $("#flagForBase64WithTS").is(':checked');
  var base64ToSign = $("#base64ToSignWithTS").val();
  if (base64ToSign !== null && base64ToSign !== "") {
    $.blockUI();
    createCMSSignatureFromBase64(selectedStorage, "SIGNATURE", base64ToSign, flag, "createCMSSignatureFromBase64Back");
  } else {
    alert("Нет данных для подписи!");
  }
}

function createCMSSignatureFromBase64Back(result) {
  $.unblockUI();
  if (result['code'] === "500") {
    alert(result['message']);
  } else if (result['code'] === "200") {
    var res = result['responseObject'];
    $("#createdCMSforBase64WithTS").val(res);
  }
}
