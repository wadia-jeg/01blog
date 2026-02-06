import { Component } from '@angular/core';

@Component({
  selector: 'app-post-feed',
  imports: [],
  templateUrl: './post-feed.html',
  styleUrl: './post-feed.scss',
})
export class PostFeed {}

/*
@Component({
  selector: 'app-post-feed',
  templateUrl: './post-feed.component.html',
  styleUrls: ['./post-feed.component.css']
})
export class PostFeedComponent implements OnInit {
  posts: Post[] = [];
  loading = true;

  constructor(private postService: PostService) {}

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts() {
    this.postService.getPosts().subscribe({
      next: (data) => {
        this.posts = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading posts', err);
        this.loading = false;
      }
    });
  }
}

*/
