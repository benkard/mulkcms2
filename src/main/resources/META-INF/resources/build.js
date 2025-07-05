const esbuild = require('esbuild');
const fs = require('fs');
const path = require('path');

const isWatch = process.argv.includes('--watch');

// Copy static assets from node_modules
function copyAssets() {
  const assets = [
    // ContentTools
    { from: 'node_modules/ContentTools/build/content-tools.min.css', to: 'web_modules/ContentTools/build/content-tools.min.css' },
    { from: 'node_modules/ContentTools/build/content-tools.min.js', to: 'web_modules/ContentTools/build/content-tools.min.js' },

    // CSS libraries
    { from: 'node_modules/normalize-opentype.css/normalize-opentype.css', to: 'web_modules/normalize-opentype.css/normalize-opentype.css' },
    { from: 'node_modules/normalize.css/normalize.css', to: 'web_modules/normalize.css/normalize.css' },
    { from: 'node_modules/purecss/build/base.css', to: 'web_modules/purecss/build/base.css' },
    { from: 'node_modules/purecss/build/buttons.css', to: 'web_modules/purecss/build/buttons.css' },
    { from: 'node_modules/purecss/build/forms.css', to: 'web_modules/purecss/build/forms.css' },
    { from: 'node_modules/purecss/build/menus.css', to: 'web_modules/purecss/build/menus.css' },
    { from: 'node_modules/purecss/build/tables.css', to: 'web_modules/purecss/build/tables.css' },
    { from: 'node_modules/sanitize.css/sanitize.css', to: 'web_modules/sanitize.css/sanitize.css' },
    { from: 'node_modules/sanitize.css/forms.css', to: 'web_modules/sanitize.css/forms.css' },
    { from: 'node_modules/sanitize.css/formsize.css', to: 'web_modules/sanitize.css/formsize.css' },
    { from: 'node_modules/sanitize.css/typography.css', to: 'web_modules/sanitize.css/typography.css' },
    { from: 'node_modules/sanitize.css/page.css', to: 'web_modules/sanitize.css/page.css' },
  ];

  assets.forEach(asset => {
    const destDir = path.dirname(asset.to);
    if (!fs.existsSync(destDir)) {
      fs.mkdirSync(destDir, { recursive: true });
    }
    if (fs.existsSync(asset.from)) {
      fs.copyFileSync(asset.from, asset.to);
    }
  });

  // Copy entire elix directory structure
  const elixSourceDir = 'node_modules/elix';
  const elixDestDir = 'web_modules/elix';
  if (fs.existsSync(elixSourceDir)) {
    // Copy recursively
    function copyRecursive(src, dest) {
      if (!fs.existsSync(dest)) {
        fs.mkdirSync(dest, { recursive: true });
      }
      const files = fs.readdirSync(src);
      files.forEach(file => {
        const srcPath = path.join(src, file);
        const destPath = path.join(dest, file);
        const stat = fs.statSync(srcPath);
        if (stat.isDirectory()) {
          copyRecursive(srcPath, destPath);
        } else if (file.endsWith('.js') || file.endsWith('.d.ts')) {
          fs.copyFileSync(srcPath, destPath);
        }
      });
    }
    copyRecursive(elixSourceDir, elixDestDir);
  }

  // Copy prismjs
  const prismDir = 'node_modules/prismjs';
  const prismDest = 'web_modules/prismjs';
  if (fs.existsSync(prismDir)) {
    if (!fs.existsSync(prismDest)) {
      fs.mkdirSync(prismDest, { recursive: true });
    }
    // Copy main files
    ['prism.js', 'prism.css'].forEach(file => {
      const src = path.join(prismDir, file);
      if (fs.existsSync(src)) {
        fs.copyFileSync(src, path.join(prismDest, file));
      }
    });
  }

  // Copy bosonic
  const bosonicDir = 'node_modules/bosonic';
  const bosonicDest = 'web_modules/bosonic';
  if (fs.existsSync(bosonicDir)) {
    if (!fs.existsSync(bosonicDest)) {
      fs.mkdirSync(bosonicDest, { recursive: true });
    }
    const files = fs.readdirSync(bosonicDir);
    files.forEach(file => {
      if (file.endsWith('.js') || file.endsWith('.css')) {
        fs.copyFileSync(path.join(bosonicDir, file), path.join(bosonicDest, file));
      }
    });
  }

  // Copy ContentTools images
  const contentToolsImagesDir = 'node_modules/ContentTools/build/images';
  const contentToolsImagesDest = 'web_modules/ContentTools/build/images';
  if (fs.existsSync(contentToolsImagesDir)) {
    if (!fs.existsSync(contentToolsImagesDest)) {
      fs.mkdirSync(contentToolsImagesDest, { recursive: true });
    }
    const imageFiles = fs.readdirSync(contentToolsImagesDir);
    imageFiles.forEach(file => {
      fs.copyFileSync(path.join(contentToolsImagesDir, file), path.join(contentToolsImagesDest, file));
    });
  }

  console.log('Assets copied successfully');
}

async function build() {
  try {
    // Copy static assets first
    copyAssets();

    const buildOptions = {
      entryPoints: [
        './lib.js',
        './lib.css'
      ],
      bundle: true,
      outdir: 'dist',
      format: 'esm',
      target: ['es2020'],
      sourcemap: true,
      loader: {
        '.css': 'css',
        '.js': 'js',
        '.woff': 'file',
        '.woff2': 'file',
        '.ttf': 'file',
        '.eot': 'file',
        '.svg': 'file',
        '.png': 'file',
        '.jpg': 'file',
        '.jpeg': 'file',
        '.gif': 'file'
      },
      define: {
        'process.env.NODE_ENV': '"production"'
      },
      plugins: [
        {
          name: 'babel-flow',
          setup(build) {
            const babel = require('@babel/core');

            build.onLoad({ filter: /\.js$/ }, async (args) => {
              const source = await fs.promises.readFile(args.path, 'utf8');

              // Skip transformation for node_modules
              if (args.path.includes('node_modules')) {
                return { contents: source, loader: 'js' };
              }

              try {
                const result = babel.transformSync(source, {
                  filename: args.path,
                  plugins: ['@babel/plugin-transform-flow-strip-types'],
                  sourceMaps: true
                });

                return {
                  contents: result.code,
                  loader: 'js'
                };
              } catch (e) {
                return {
                  errors: [{
                    text: e.message,
                    location: { file: args.path }
                  }]
                };
              }
            });
          }
        }
      ]
    };

    if (isWatch) {
      const ctx = await esbuild.context(buildOptions);
      await ctx.watch();
      console.log('Watching for changes...');
    } else {
      await esbuild.build(buildOptions);
      console.log('Build completed successfully');
    }

  } catch (error) {
    console.error('Build failed:', error);
    process.exit(1);
  }
}

build();
