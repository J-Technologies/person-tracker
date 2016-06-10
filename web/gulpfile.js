var gulp = require('gulp');
var sass = require('gulp-sass');
var browserSync = require('browser-sync').create();

gulp.task('default', ['watch'], function () {
    // place code for your default task here
});

gulp.task('watch', ['browserSync', 'sass', 'copy-js', 'copy-index-html'], function () {
    gulp.watch('src/scss/**/*.scss', ['sass']);

    gulp.watch('src/*.html', ['copy-index-html']);
    gulp.watch('src/js/**/*.js', ['copy-js']);
});

gulp.task('sass', function () {
    return gulp.src('src/scss/**/*.scss')
        .pipe(sass())
        .pipe(gulp.dest('dist/css'))
        .pipe(browserSync.reload({stream: true}))
});

gulp.task('copy-index-html', function() {
    gulp.src('src/index.html')
        .pipe(gulp.dest('dist'))
        .pipe(browserSync.reload({stream: true}))
});

gulp.task('copy-js', function() {
    gulp.src('src/js/**/*.js')
        .pipe(gulp.dest('dist/js'))
        .pipe(browserSync.reload({stream: true}))
});

gulp.task('browserSync', function () {
    browserSync.init({
        server: {
            baseDir: 'dist'
        }
    })
});