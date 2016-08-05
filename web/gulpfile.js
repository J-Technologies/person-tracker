var gulp = require('gulp');
var sass = require('gulp-sass');
var del = require('del');
var browserSync = require('browser-sync').create();

var fs = require("fs");
var browserify = require("browserify");

if (!fs.existsSync('dist')) {
    fs.mkdirSync('dist');
}

gulp.task('default', ['sass', 'babelify-js', 'index-html', 'browserSync'], () => {
    gulp.src('src/scss/vendor/*.css').pipe(gulp.dest('dist/css/vendor'));

    gulp.watch('src/scss/**/*.scss', ['sass']);
    gulp.watch('src/*.html', ['index-html']);
    gulp.watch('src/js/**/*.js', ['babelify-js']);
});

gulp.task('sass', () => gulp.src('src/scss/**/*.scss')
        .pipe(sass())
        .pipe(gulp.dest('dist/css'))
        .pipe(browserSync.reload({stream: true})));

gulp.task('index-html', () => gulp.src('src/index.html')
        .pipe(gulp.dest('dist'))
        .pipe(browserSync.reload({stream: true})));

gulp.task('browserSync', () => browserSync.init({
        server: {
            baseDir: 'dist'
        }
    }));

gulp.task('babelify-js', () => browserify("src/js/index.js")
    .transform("babelify", {presets: ["es2015", "react"]})
    .bundle()
    .pipe(fs.createWriteStream("dist/bundle.js")));


gulp.task('release', ['sass', 'babelify-js', 'index-html']);

gulp.task('clean', () => del(['dist/**/*', 'dist']));