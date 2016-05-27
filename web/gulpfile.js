var gulp = require('gulp');
var sass = require('gulp-sass');
var browserSync = require('browser-sync').create();

gulp.task('default', function () {
    // place code for your default task here
});

gulp.task('watch', ['browserSync', 'sass'], function () {
    gulp.watch('src/scss/**/*.scss', ['sass']);

    gulp.watch('src/*.html', browserSync.reload);
    gulp.watch('src/js/**/*.js', browserSync.reload);
});

gulp.task('sass', function () {
    return gulp.src('src/scss/**/*.scss')
        .pipe(sass())
        .pipe(gulp.dest('dist/css'))
        .pipe(browserSync.reload({stream: true}))
});

gulp.task('browserSync', function () {
    browserSync.init({
        server: {
            baseDir: 'dist'
        }
    })
});