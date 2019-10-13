"use strict";

$(document).ready( function() {
	
	$.fn.initAnimation = function() {
		$(this).each( function() {
			var animation_class = $(this).data('animate');
			var delay = $(this).data('delay');
			delay = delay? delay : 0;
			var obj = $(this);
			setTimeout( function() {
				obj.removeClass('not-animated').addClass(animation_class).addClass('animated');
				obj.removeClass('hide-iframe');
				obj.css( 'visibility', 'visible' );
			}, delay );
		} );
	};
	$.fn.resetAnimation = function() {
		$(this).each( function() {
			var animation_class = $(this).data('animate');
			$(this).removeClass(animation_class).removeClass('animated').addClass('not-animated');
		} );
	};
	$.fn.progressbar_animate = function() {
		$(this).each( function() {
			var progressbar = $(this);
			var value = progressbar.data('value');
			var meter = progressbar.find('.progressbar-meter');
			meter.removeClass('has-animation').css( 'width', '0' );
			setTimeout( function() {
				meter.addClass('has-animation').css( 'width', value + '%' );
			}, 300 );
			progressbar.find('.progressbar-value span').countTo( {
				from: 0,
		        to: value,
		        speed: 1500,
		        refreshInterval: 1500 / value
			} );
		} );
	}
	
	function init() {

		/* CSS Animation with waypoint */
		$('#content [data-animate]').each( function() {
			var element = $(this);
			element.waypoint( function() {
				element.initAnimation();
			}, {
				triggerOnce: true,
				offset: 'bottom-in-view'
			} );
		} );
		
		/* HTML5 video play */
		if( Modernizr.video ) {
			/* Play/pause toggle */
			$('.play-pause-control').on( "click", function() {
				var video_container = $(this).closest('.video-container');
				if( video_container.hasClass('paused') ) {
					video_container.find('video')[0].play();
					video_container.removeClass('paused');
				} else {
					video_container.find('video')[0].pause();
					video_container.addClass('paused');
				}
			} );
			/* On playback ended event */
			$('video').each( function() {
				var video = $(this)[0];
				video.onended = function(e) {
					$(video).closest('.video-container').addClass('paused');
				};
			} );
		} else {
			$('.play-pause-control').hide();
		}
		
		/* init code here */
		/* -------------- */
		
		/* Menu toggle */
		$('#menu-toggle').on( "click", function(e) {
			e.preventDefault();
			e.stopPropagation();
			if( window.matchMedia( "(max-width: 992px)" ).matches ) {
				if( $(this).hasClass('opened') ) {
					$(this).removeClass('opened');
					$('#mobile-menu').removeClass('opened');
				} else {
					$(this).addClass('opened');
					$('#mobile-menu').addClass('opened');
				}
			}
		} );
		$('html,body').on( "click", function(e) {
			if( $('#menu-toggle').hasClass('opened') ) {
				e.preventDefault();
				e.stopPropagation();
				$('#menu-toggle').removeClass('opened');
				$('#mobile-menu').removeClass('opened');
			}
		} );
		$('#mobile-menu').on( "click", function(e) {
			e.preventDefault();
			e.stopPropagation();
		} );
		
		/* Header Search Box */
		$('.menu-icon.search').on( "click", function() {
			$('.search-box').addClass('visible');
			setTimeout( function() {
				$('.search-box .s').focus();
			}, 100 );
			return false;
		} );
		$('.search-box .s').on( "blur", function() {
			$('.search-box').removeClass('visible');
		} );
		
		/* Mobile Menu Item Click */
		$('#mobile-menu .menu-item').on( "click", function() {
			var parent_li = $(this).parent();
			if( parent_li.hasClass('opened') ) {
				parent_li.removeClass('opened');
				parent_li.children('.submenu').slideUp( 300 );
			} else {
				parent_li.addClass('opened');
				parent_li.children('.submenu').slideDown( 300 );
			}
			return false;
		} );
		$('#mobile-menu .sub-menu-item a').on( "click", function() {
			window.location = $(this).attr('href');
		} );
		
		/* Category Selector */
		$('.category-selector .category').on( "click", function(e) {
			e.preventDefault();
			e.stopPropagation();
			var filter = $(this).data('filter');
			if( filter != '*' ) {
				filter = '.' + filter;
			}
			var container = $(this).closest('.our-work-isotope-wrapper').find('.our-works-container');
			container.isotope( {
				filter: filter
			} );
		} );
		
		/* Skill Percent Number Count */
		$('.skill-percent').each( function() {
			var counter = $(this);
			counter.waypoint( function() {
				counter.countTo();
			}, {
				triggerOnce: true,
				offset: 'bottom-in-view'
			} );
		} );
		
		/* Progress Bar */
		$('.progressbar').each( function() {
			$(this).waypoint( function() {
				$(this).progressbar_animate();
			}, {
				triggerOnce: true,
				offset: 'bottom-in-view'
			} );
		} );
		
		/* Features Tab */
		$('.features-tab-wrapper').each( function() {
			$(this).find('.tab-link').on( 'click', function() {
				$(this).tab('show');
				$($(this).attr('href')).find('.progressbar').progressbar_animate();
				return false;
			} );
		} );
	}

	/* Initialization */
	init();

	/* 
	 * Initialization after images loaded
	 */
	imagesLoaded( $('body'), function() {
		/* Main slider init */
		if( $('#main-slider').length > 0 ) {
			var main_slider = new Swiper( '#main-slider .swiper-container', {
				direction: 'horizontal',
				loop: true,
				speed: 600,
				autoplay: 6000,
				allowSwipeToPrev: false,
				allowSwipeToNext: false,
				preventClicksPropagation: false,
				nextButton: '.main-slider-control.next',
				prevButton: '.main-slider-control.prev',
				pagination: '.page-controls',
				paginationClickable: true,
				paginationBulletRender: function (index, className) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				},
				onInit: function( swiper ) {
					var current_slide = $(swiper.slides[swiper.activeIndex]);
					current_slide.find('[data-animate]').initAnimation();
				},
				onSlideChangeStart: function( swiper ) {
					var current_slide = $(swiper.slides[swiper.activeIndex]);
					current_slide.find('[data-animate]').resetAnimation();
				},
				onSlideChangeEnd: function( swiper ) {
					var current_slide = $(swiper.slides[swiper.activeIndex]);
					swiper.slides.find('[data-animate]').css( 'visibility', 'hidden' );
					current_slide.find('[data-animate]').initAnimation();
				}
			} );
		}
		
		/* Our Work Carousel Responsive Init */
		our_work_carousel_responsive_init();
		
		/* About Us Slider */
		if( $('.our-feature-slider').length > 0 ) {
			var about_us_swiper = new Swiper( '.our-feature-slider', {
				direction: 'horizontal',
				loop: true,
				speed: 600,
				autoplay: 5000,
				pagination: '.our-feature-slider-controls',
				paginationClickable: true,
				paginationBulletRender: function (index, className) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				}
			} );
		}
		
		/* About Us Slider - Home 4 */
		if( $('.about-us-h4-slider').length > 0 ) {
			var about_us_h4_slider = new Swiper( '.about-us-h4-slider .swiper-container', {
				direction: 'horizontal',
				slideToClickedSlide: true,
				autoplay: 5000,
				speed: 600,
				pagination: '.about-us-h4-slider .page-controls',
				paginationClickable: true,
				paginationBulletRender: function( index, className ) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				}
			} );
			$('.about-us-h4-slider-thumb .thumb').on( 'click', function() {
				var index = $(this).data('index');
				if( index >= 0 && index < about_us_h4_slider.slides.length ) {
					about_us_h4_slider.slideTo( index );
				}
			} );
		}
		
		/* Testimonial Slider - Home 4 */
		if( $('.testimonial-slider').length > 0 ) {
			var testimonial_swiper = new Swiper( '.testimonial-slider', {
				direction: 'horizontal',
				slideToClickedSlide: true,
				speed: 600,
				autoplay: 5000,
				loop: true,
				spaceBetween: 40,
				pagination: '.testimonial-slider-page-controls',
				paginationClickable: true,
				paginationBulletRender: function( index, className ) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				}
			} );
		}
		
		/* iMac-Frame Portfolio Slider - Home 5 */
		if( $('.imac-frame-portfolio-slider').length > 0 ) {
			var imac_frame_portfolio_slider = new Swiper( '.imac-frame-portfolio-slider', {
				direction: 'horizontal',
				slideToClickedSlide: true,
				speed: 600,
				autoplay: 5000,
				loop: true,
				spaceBetween: 20,
				pagination: '.imac-frame-slider-controls',
				paginationClickable: true,
				paginationBulletRender: function( index, className ) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				}
			} );
		}
		
		/* New Features Slider - Home 6 */
		if( $('.new-features-slider').length > 0 ) {
			var imac_frame_portfolio_slider = new Swiper( '.new-features-slider', {
				direction: 'horizontal',
				slideToClickedSlide: true,
				speed: 600,
				autoplay: 5000,
				loop: true,
				spaceBetween: 20,
				pagination: '.new-features-slider-controls',
				paginationClickable: true,
				paginationBulletRender: function( index, className ) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				}
			} );
		}
		
		/* Portfolio Single Post 1 Slider */
		if( $('.portfolio-single-slider1').length > 0 ) {
			var portfolio_single_slider1 = new Swiper( '.portfolio-single-slider1', {
				direction: 'horizontal',
				slideToClickedSlide: true,
				speed: 600,
				autoplay: 5000,
				loop: true,
				pagination: '.portfolio-single-slider-controls',
				paginationClickable: true,
				paginationBulletRender: function( index, className ) {
					return '<a class="page-control ' + className + '" href="#"></a>';
				}
			} );
		}
		
		/* Portfolio Single Post 2 Slider */
		if( $('.portfolio-single2-team-slider').length > 0 ) {
			var portfolio_single_slider2 = new Swiper( '.portfolio-single2-team-slider .swiper-container', {
				direction: 'horizontal',
				slideToClickedSlide: true,
				speed: 600,
				autoplay: 5000,
				loop: true,
				spaceBetween: 3,
				slidesPerView: 'auto',
				loopedSlides: 10,
				nextButton: '.portfolio-single2-team-slider-control.next',
				prevButton: '.portfolio-single2-team-slider-control.prev'
			} );
			portfolio_single2_team_slider_resize();
		}
		
		/* Logos Carousel Responsive Init */
		logos_carousel_responsive_init();
		
		/* Preloader finish */
		$('body').removeClass('loading');
		$('#preloader').hide();
		$('#preloader-wrapper').css( 'opacity', '0' );
		setTimeout( function() {
			$('#preloader-wrapper').hide();
		}, 800 );
		
		/* Our Works Container Isotope */
		our_works_isotope();
		
		/* Blog Grid Style Isotope */
		blog_grid_isotope();

	} );
	
} );

$(window).scroll( function() {
	
} );

$(window).resize(function(){
	/* Our Works Container Isotope */
	our_works_isotope();
});

$(window).on( "throttledresize", function() {
	/* Our Work Carousel Responsive Init */
	our_work_carousel_responsive_init();

	/* Logos Carousel Responsive Init */
	logos_carousel_responsive_init();
	
	/* Portfolio Single 2 Team Slider Responsive Resize */
	portfolio_single2_team_slider_resize();
} );

var logos_slides = 0;
var logos_carousel = false;
function logos_carousel_responsive_init() {
	if( $('.logos-carousel').length > 0 ) {
		var wrapper_width = $('.logos-wrapper').width();
		var item_width = 115;
		var item_margin = 55;
		var slides = Math.floor( ( wrapper_width + item_margin ) / ( item_width + item_margin ) );
		if( slides != logos_slides ) {
			if( logos_carousel != false ) {
				logos_carousel.destroy();
			}
			logos_slides = slides;
			$('.logos-carousel').width( slides * item_width + ( slides - 1 ) * item_margin );
			$('.logos-carousel').find( '.swiper-slide' ).css( 'margin-right', 0 );
			logos_carousel = new Swiper( '.logos-carousel', {
				direction: 'horizontal',
				loop: true,
				speed: 600,
				autoplay: 3000,
				slidesPerView: slides,
				spaceBetween: item_margin
			} );
		}
	}
}

var our_work_slides = 0;
var our_work_carousel = false;
function our_work_carousel_responsive_init() {
	if( $('.our-work-carousel-wrapper').length > 0 ) {
		var wrapper_width = $('.our-work-carousel-wrapper').width();
		var item_width = $('#our-work-carousel .our-work-item').width();
		var item_margin = 20;
		var slides = Math.floor( ( wrapper_width + item_margin ) / ( item_width + item_margin ) );
		if( slides != our_work_slides ) {
			if( our_work_carousel != false ) {
				our_work_carousel.destroy();
			}
			our_work_slides = slides;
			$('#our-work-carousel').width( slides * item_width + ( slides - 1 ) * item_margin );
			$('#our-work-carousel').find( '.swiper-slide' ).css( 'margin-right', 0 );
			our_work_carousel = new Swiper( '#our-work-carousel', {
				direction: 'horizontal',
				loop: true,
				speed: 600,
				autoplay: 5000,
				slidesPerView: slides,
				spaceBetween: item_margin,
				prevButton: '.our-work-carousel-control.prev',
				nextButton: '.our-work-carousel-control.next'
			} );
		}
	}
}

function our_works_isotope() {
	function do_isotope( container ) {
		var columns = container.data('columns');
		var margin = container.data('margin');
		var ratio = 1.422;
		var container_width = container[0].getBoundingClientRect().width;
		var large = false;

		if( container.hasClass('large') ) {
			large = true;
			ratio = 1;
		}
		if( container_width <= 1100 ) {
			if( large ) {
				if( container_width <= 400 ) {
					columns = 1;
				}
			} else {
				columns = Math.floor( container_width / 280 );
				var item_width_tmp = ( container_width + margin ) / columns - margin;
				if( item_width_tmp > 384 ) {
					columns += 1;
				}
			}
		}
		var item_width = ( container_width + margin ) / columns - margin;
		var item_height = Math.floor( item_width / ratio );
		container.find('.work').width( item_width );
		container.find('.work .work-image').width( item_width ).height( item_height );
		container.css( 'visibility', 'visible' );
		container.isotope( {
			layoutMode: 'fitRows',
			itemSelector: '.work',
			fitRows: {
				itemWidth: item_width,
				gutter: margin
			}
		} );
	}
	$('.our-works-container').each( function() {
		do_isotope( $(this) );
	} );
}

function blog_grid_isotope() {
	$('.blog-grid-wrapper').each( function() {
		var container = $(this);
		container.css( 'visibility', 'visible' );
		container.isotope( {
			itemSelector: '.post-wrapper'
		} );
	} );
}

function portfolio_single2_team_slider_resize() {
	if( $('.portfolio-single2-team-slider').length > 0 ) {
		var slider_wrapper = $('.portfolio-single2-team-slider');
		var slider_width = slider_wrapper[0].getBoundingClientRect().width;
		var item_width = 259;
		var items_per_view = Math.floor( ( slider_width - 1 ) / item_width ); 
		var control_width = ( slider_width - 1 - item_width * items_per_view ) / 2;
		if( control_width < 50 && items_per_view > 1 ) {
			items_per_view -= 1;
			control_width = ( slider_width - 1 - item_width * items_per_view ) / 2;
		}
		slider_wrapper.find('.portfolio-single2-team-slider-control').css( 'width', control_width );
		slider_wrapper.find('.team-slider-wrapper').css( 'margin-left', control_width + 2 );
		slider_wrapper.find('.team-slider-wrapper').css( 'margin-right', control_width + 2 );
	}
}
