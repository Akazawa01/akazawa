<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>従業員メニュー</title>
<link rel="stylesheet" href="style.css">





<style>
/* カレンダー用の簡単なスタイル */
#calendar {
  max-width: 600px;
  margin: 20px auto;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
}
#calendar table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}
#calendar th, #calendar td {
  padding: 5px;
  border: 1px solid #ccc;
}
#calendar th {
  background-color: #eee;
}
button.aaaaa {
		background-color: #007bff;
	    color: white;
	    padding: 12px 25px;
	    border: none;
	    border-radius: 6px;
	    cursor: pointer;
	    font-size: 1.1em;
	    transition: background-color 0.3s ease, transform 0.2s ease;
	    display: inline-block;
	    margin: 0 10px;
}
</style>
</head>
<body>

<div class="container">
  <h1>従業員メニュー</h1>
  <p>ようこそ, ${user.username}さん</p>

  <c:if test="${not empty sessionScope.successMessage}">
    <p class="success-message"><c:out value="${sessionScope.successMessage}"/></p>
    <c:remove var="successMessage" scope="session"/>
  </c:if>

  <div class="button-group">
    <form action="attendance" method="post" style="display:inline;">
      <input type="hidden" name="action" value="check_in">
      <input type="submit" value="出勤">
    </form>
    <form action="attendance" method="post" style="display:inline;">
      <input type="hidden" name="action" value="check_out">
      <input type="submit" value="退勤">
    </form>
  </div>
  
  <h2>チャット</h2>
<div id="chat-box" style="border:1px solid #ccc; height:200px; overflow-y:auto; padding:10px; background:#f0f0f0;"></div>

<form id="chat-form" action="chat" method="post" style="margin-top:10px;">
  <input type="text" name="msg" id="chat-input" placeholder="メッセージを入力..." style="width:70%;">
  <input type="hidden" name="user" value="${user.username}">
  <button type="submit" class="aaaaa">送信</button>
</form>
<script>
const chatBox = document.getElementById("chat-box");
const chatForm = document.getElementById("chat-form");
const chatInput = document.getElementById("chat-input");

// メッセージ一覧を取得して表示
function loadMessages() {
  fetch("chat")
    .then(res => res.text())
    .then(data => {
      chatBox.innerText = data; // サーバーからの内容をそのまま表示
      chatBox.scrollTop = chatBox.scrollHeight; // 自動で一番下までスクロール
    });
}

// メッセージ送信処理
chatForm.addEventListener("submit", function(e) {
  e.preventDefault(); // ページリロード防止
  const msg = chatInput.value;
  if (!msg.trim()) return;

  fetch("chat", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: "user=${user.username}&msg=" + encodeURIComponent(msg)
  })
  .then(() => {
    chatInput.value = "";
    loadMessages(); // 再読み込み
  });
});

// ページ読み込み時にチャット内容表示
window.onload = function() {
  loadMessages();
  setInterval(loadMessages, 3000); // 3秒ごとに更新（オートリロード）
};
</script>


  <h2>勤怠カレンダー</h2>
  <div id="calendar"></div>


  <script>
    const attendanceDates = [
      <c:forEach var="att" items="${attendanceRecords}">
        '${fn:substring(att.checkInTime, 8, 10)}',
      </c:forEach>
    ];

    window.onload = function() {
      const calendar = document.getElementById('calendar');
      const now = new Date();
      const year = now.getFullYear();
      const month = now.getMonth(); // 0 = 1月
      const firstDay = new Date(year, month, 1).getDay();
      const lastDate = new Date(year, month + 1, 0).getDate();

      let html = '<table>';
      html += '<tr><th colspan="7">' + year + '年 ' + (month + 1) + '月</th></tr>';
      html += '<tr><th>日</th><th>月</th><th>火</th><th>水</th><th>木</th><th>金</th><th>土</th></tr><tr>';

      // 空白セル
      for(let i=0; i<firstDay; i++) {
        html += '<td></td>';
      }

      // 日付
      for(let date=1; date<=lastDate; date++) {
        let dayStr = (date < 10 ? '0' : '') + date; // '01','02',...
        if(attendanceDates.includes(dayStr)) {
          html += '<td style="background-color:#007bff; border-radius:7%; color:white;">' + date + '</td>';
        } else {
          html += '<td>' + date + '</td>';
        }
        if((date + firstDay) % 7 === 0) html += '</tr><tr>';
      }

      html += '</tr></table>';
      calendar.innerHTML = html;
    };
  </script>

  <h2>あなたの勤怠履歴</h2>
  
  <table>
    <thead>
      <tr>
        <th>出勤時刻</th>
        <th>退勤時刻</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="att" items="${attendanceRecords}">
        <tr>
          <td>${att.checkInTime}</td>
          <td>${att.checkOutTime}</td>
        </tr>
      </c:forEach>
      <c:if test="${empty attendanceRecords}">
        <tr><td colspan="2">勤怠記録がありません。</td></tr>
      </c:if>
    </tbody>
  </table>

  <div class="button-group">
    <a href="logout" class="button secondary">ログアウト</a>
  </div>
</div>
</body>
</html>

