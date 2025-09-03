<h1>マイページ</h1>


<p><a href="attendance">勤怠一覧ページへ</a></p>

<table border="1">
  <tr>
    <th>日付</th>
    <th>出勤</th>
    <th>退勤</th>
    <th>勤務時間</th>
    <th>ステータス</th>
  </tr>
  <c:forEach var="att" items="${attendances}">
      <tr>
          <td>${att.date}</td>
          <td>${att.clockIn}</td>
          <td>${att.clockOut}</td>
          <td>${att.workHours}</td>
          <td style="color: ${att.statusColor}">${att.status}</td>
      </tr>
  </c:forEach>
</table>
