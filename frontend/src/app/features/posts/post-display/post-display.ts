import { Component } from '@angular/core';

@Component({
  selector: 'app-post-display',
  imports: [],
  templateUrl: './post-display.html',
  styleUrl: './post-display.scss',
})
export class PostDisplay {}

/*
@Component({
  selector: 'app-post-display',
  templateUrl: './post-display.component.html',
  styleUrls: ['./post-display.component.css']
})
export class PostDisplayComponent implements OnInit {
  post: Post;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.loadPost(postId);
    }
  }

  loadPost(id: string) {
    this.postService.getPostById(id).subscribe({
      next: (data) => {
        this.post = data;
      },
      error: (err) => {
        console.error('Failed to load post', err);
      }
    });
  }
}

*/
