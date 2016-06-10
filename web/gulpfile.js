var gulp = require('gulp');
var sass = require('gulp-sass');
var clean = require('gulp-clean');
var browserSync = require('browser-sync').create();

var fs = require("fs");
var browserify = require("browserify");


gulp.task('default', ['clean', 'watch'], function () {
    // place code for your default task here
});

gulp.task('clean', function () {
    return gulp.src('dist', {read: false}).pipe(clean());
});

gulp.task('watch', ['browserSync', 'sass', 'babelify-js', 'index-html'], function () {
    gulp.watch('src/scss/**/*.scss', ['sass']);

    gulp.watch('src/*.html', ['index-html']);
    gulp.watch('src/js/**/*.js', ['babelify-js']);
});

gulp.task('sass', function () {
    return gulp.src('src/scss/**/*.scss')
        .pipe(sass())
        .pipe(gulp.dest('dist/css'))
        .pipe(browserSync.reload({stream: true}))
});

gulp.task('index-html', function () {
    gulp.src('src/index.html')
        .pipe(gulp.dest('dist'))
        .pipe(browserSync.reload({stream: true}))
});

gulp.task('browserSync', function () {
    browserSync.init({
        server: {
            baseDir: 'dist'
        }
    })
});

gulp.task('babelify-js', function () {
    browserify("src/js/index.js")
        .transform("babelify", {presets: ["es2015", "react"]})
        .bundle()
        .pipe(fs.createWriteStream("dist/js/bundle.js"))
});
