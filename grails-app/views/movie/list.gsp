<!DOCTYPE html>
<html>
    <head>
        <title>Movie Page</title>
    </head>
    <body>
    <h1>Movies!</h1>
    <h2>We have ${movies.size()} movies</h2>
        <div id="movies">
            <g:each in="${movies}" var="movie">
                <q>${movie.title}</q>
                <img src="${movie.imageURL}" alt="${movie.title}" >
            </g:each>
        </div>
    </body>
</html>
