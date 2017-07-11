<!DOCTYPE html>
<html>
<head>
    <title>Movie Page</title>
</head>

<body>
<h1>Movies!</h1>

<h2>We have ${movies.size()} movies</h2>

<div id="movies">
    <table>
        <tr>
            <g:each in="${movies}" var="movie">
                <td>
                    <img src="${movie.artURL}" alt="${movie.title}">
                    <h3>${movie.title}</h3>
                </td>
            </g:each>
        </tr>
    </table>
</div>
</body>
</html>
