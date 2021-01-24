<form action="${action}" method="${method}">
    <#list inputs as input>
        <input type="${input.type}" name="${input.name}" placeholder="${input.placeholder}">
    </#list>
</form>